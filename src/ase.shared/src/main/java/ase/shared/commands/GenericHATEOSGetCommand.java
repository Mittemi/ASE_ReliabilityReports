package ase.shared.commands;

import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Created by Michael on 20.06.2015.
 */
public abstract class GenericHATEOSGetCommand<TResult extends Resources<TModel>, TModel> {

    private Traverson traverson;
    private Traverson.TraversalBuilder traversalBuilder;
    private Class<TResult> clazzResult;

    public GenericHATEOSGetCommand(Class<TResult> clazzResult, String uri) {

        this.clazzResult = clazzResult;
        traverson = new Traverson(URI.create(uri), HAL_JSON, APPLICATION_JSON);
    }

    protected abstract Traverson.TraversalBuilder initBuilder(Traverson traverson);

    public Collection<TModel> execute() {
        traversalBuilder = initBuilder(traverson);
        return traversalBuilder.toObject(clazzResult).getContent();
    }

    public TModel getSingleResult() {
        Collection<TModel> execute = execute();
        if(execute == null || execute.size() == 0)
            return null;

        if(execute.size() > 1) {

            System.out.println("Too many results, only 1 expected!");
            throw new IllegalArgumentException("Too many results, only 1 expected!");
        }

        return execute.iterator().next();
    }

    public List<TModel> toList() {
        Collection<TModel> result = execute();

        return result.stream().collect(Collectors.toList());
    }
}
