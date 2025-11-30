package es.deusto.sd.ecoembes.external;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.external.ExternalPlantInfo;
import es.deusto.sd.ecoembes.external.IExternalRecyclingGateway;

@Component
public class RestRecyclingGateway implements IExternalRecyclingGateway {

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private final String BASE_URL = "http://localhost:8085/api"; // cambiar según planta

    @Override
    public Optional<ExternalPlantInfo> getPlantInfo(long plantId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/plants/" + plantId))
                    .GET()
                    .build();

            String body = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            ExternalPlantInfo info = mapper.readValue(body, ExternalPlantInfo.class);
            return Optional.of(info);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean sendAssignment(ExternalAssignmentDTO dto) {
        try {
            String json = mapper.writeValueAsString(dto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/assignment"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;

        } catch (Exception e) {
            return false;
        }
    }
}
