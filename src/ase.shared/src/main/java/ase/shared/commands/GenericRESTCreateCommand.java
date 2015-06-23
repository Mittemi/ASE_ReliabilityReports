package ase.shared.commands;

import ase.shared.commands.reportstorage.CreateReportCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by Michael on 23.06.2015.
 */
public abstract class GenericRESTCreateCommand <TRequest> extends GenericRESTCommand<TRequest, CreateResult> {

    protected GenericRESTCreateCommand() {
        // we don't care about the type
        super(new ParameterizedTypeReference<CreateResult>() {
        });
    }

    protected abstract RequestEntity<TRequest> getRequest();

    @Override
    public CreateResult getResult() {

        ResponseEntity<CreateResult> result = execute();

        URI location = result.getHeaders().getLocation();

        CreateResult createResult = new CreateResult();
        createResult.setOk(result.getStatusCode().is2xxSuccessful());
        createResult.setLocation(location.getPath());
        return createResult;
    }
}
