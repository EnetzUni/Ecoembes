package es.deusto.sd.ecoembes.external;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import es.deusto.sd.ecoembes.dto.CapacityResponseDTO;

@Component
public class RestRecyclingGateway implements IExternalRecyclingGateway {

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private final String BASE_URL = "http://localhost:8081/api/capacity";

    public Optional<Float> getCapacity(CapacityRequestDTO requestDto) {
        try {
            String jsonRequest = mapper.writeValueAsString(requestDto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return Optional.empty();
            }

            CapacityResponseDTO responseDto =
                    mapper.readValue(response.body(), CapacityResponseDTO.class);

            return Optional.of(responseDto.getCapacity());

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
