package ase.shared.commands;

import ase.shared.Constants;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resources;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

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
