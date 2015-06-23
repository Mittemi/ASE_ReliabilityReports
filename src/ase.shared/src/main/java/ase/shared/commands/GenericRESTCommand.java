package ase.shared.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Michael on 22.06.2015.
 */
public abstract class GenericRESTCommand<TRequest, TModel> {

    @Autowired
    private RestTemplate restTemplate;

    private ParameterizedTypeReference<TModel> typeReference;

    protected GenericRESTCommand(ParameterizedTypeReference<TModel> typeReference) {

        this.typeReference = typeReference;
    }

    protected abstract RequestEntity<TRequest> getRequest();

    public ResponseEntity<TModel> execute() {
        return restTemplate.exchange(getRequest(), typeReference);
    }

    public TModel getResult() {
        ResponseEntity<TModel> body = execute();
        return body.getBody();
    }
}
