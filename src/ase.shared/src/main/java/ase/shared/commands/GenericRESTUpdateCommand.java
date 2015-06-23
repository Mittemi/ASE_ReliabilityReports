package ase.shared.commands;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Created by Michael on 23.06.2015.
 */
public abstract class GenericRESTUpdateCommand <TRequest> extends GenericRESTCommand<TRequest, UpdateResult> {

    protected GenericRESTUpdateCommand() {
        // we don't care about the type
        super(new ParameterizedTypeReference<UpdateResult>() {
        });
    }

    protected abstract RequestEntity<TRequest> getRequest();

    @Override
    public UpdateResult getResult() {

        ResponseEntity<UpdateResult> result = execute();

        UpdateResult updateResult = new UpdateResult();
        updateResult.setOk(result.getStatusCode().is2xxSuccessful());
        return updateResult;
    }
}
