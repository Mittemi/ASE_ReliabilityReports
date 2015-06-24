package ase.shared.commands;

import ase.shared.ASEModelMapper;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Created by Michael on 20.06.2015.
 */
public abstract class GenericHATEOSFindCommand<TResult extends Resources<? extends ResourceSupport>, TModel> {

    @Autowired
    private ASEModelMapper modelMapper;

    private Traverson traverson;
    private Traverson.TraversalBuilder traversalBuilder;
    private Class<TResult> clazzResult;
    private Class<TModel> targetType;

    public GenericHATEOSFindCommand(Class<TResult> clazzResult, Class<TModel> targetType, String uri) {

        this.clazzResult = clazzResult;
        this.targetType = targetType;
        traverson = new Traverson(URI.create(uri), HAL_JSON, APPLICATION_JSON);
    }

    protected abstract Traverson.TraversalBuilder initBuilder(Traverson traverson);

    public List<Pair<String, TModel>> execute() {
        traversalBuilder = initBuilder(traverson);
        Collection<? extends ResourceSupport> content = traversalBuilder.toObject(clazzResult).getContent();

        List<Pair<String,TModel>> result = new LinkedList<>();

        content.forEach(x -> {
            String id = x.getLink("self").getHref();
            result.add(new Pair<String, TModel>(id.substring(id.lastIndexOf("/") + 1), modelMapper.map(x, targetType)));
        });

        return result;
    }

    public TModel getSingleResult() {
        List<Pair<String, TModel>> execute = execute();
        if(execute == null || execute.size() == 0)
            return null;

        if(execute.size() > 1) {

            System.out.println("Too many results, only 1 expected!");
            throw new IllegalArgumentException("Too many results, only 1 expected!");
        }

        return execute.iterator().next().getValue();
    }
}
