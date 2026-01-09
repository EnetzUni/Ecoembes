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

    // --- CAMBIO CLAVE: Puerto 8081 y ruta completa ---
    private final String BASE_URL = "http://localhost:8081/api/capacities/check";

    @Override
    public Optional<Float> getCapacity(CapacityRequestDTO requestDto) {
        try {
            // Convertimos el objeto Java a JSON string
            String jsonRequest = mapper.writeValueAsString(requestDto);

            // Construimos la petición POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            // Enviamos
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // Si no es 200 OK, devolvemos vacío
            if (response.statusCode() != 200) {
                return Optional.empty();
            }

            // Leemos la respuesta
            CapacityResponseDTO responseDto =
                    mapper.readValue(response.body(), CapacityResponseDTO.class);

            return Optional.of(responseDto.getCapacity());

        } catch (Exception e) {
            System.err.println("Error conectando con PlasSB (8081): " + e.getMessage());
            return Optional.empty();
        }
    }
}