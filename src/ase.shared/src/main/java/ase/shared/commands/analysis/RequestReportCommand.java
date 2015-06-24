package ase.shared.commands.analysis;

import ase.shared.commands.GenericRESTCommand;
import ase.shared.dto.AnalysisRequestDTO;
import ase.shared.dto.AnalysisResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Created by Michael on 24.06.2015.
 */
public class RequestReportCommand extends GenericRESTCommand<AnalysisRequestDTO, AnalysisResponseDTO> {
    private AnalysisRequestDTO body;
    private String url;
    private String priority;

    public RequestReportCommand(String url, AnalysisRequestDTO body, String priority) {
        super(new ParameterizedTypeReference<AnalysisResponseDTO>() {
        });
        this.body = body;
        this.url = url;
        this.priority = priority;
    }


    @Override
    protected RequestEntity<AnalysisRequestDTO> getRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RequestEntity<AnalysisRequestDTO>(body, headers, HttpMethod.POST, URI.create(url + "/analysis/request/" + priority + "/"));
    }
}
