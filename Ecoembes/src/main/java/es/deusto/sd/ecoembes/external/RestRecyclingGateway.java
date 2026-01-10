package es.deusto.sd.ecoembes.external;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import es.deusto.sd.ecoembes.dto.CapacityResponseDTO;

@Component
public class RestRecyclingGateway implements IExternalRecyclingGateway {

    private final String API_URL = "http://localhost:8081/api/capacity/check";
    private final String PLAN_URL = "http://localhost:8081/api/plans";
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public RestRecyclingGateway() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Optional<Float> getCapacity(CapacityRequestDTO requestDto) {
        try {
            // 1. Preparar el JSON
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // --- LOG DE DEBUG (Para ver qué enviamos y a dónde) ---
            System.out.println("🤖 REST [OUT] -> Intentando conectar a: " + API_URL);
            System.out.println("🤖 REST [OUT] -> Enviando Body: " + requestBody);
            // -----------------------------------------------------

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 2. Enviar petición
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // --- LOG DE DEBUG (Para ver qué responde el servidor) ---
            System.out.println("🤖 REST [IN]  -> Status Code: " + response.statusCode());
            System.out.println("🤖 REST [IN]  -> Body Respuesta: " + response.body());
            // -----------------------------------------------------

            if (response.statusCode() == 200) {
                CapacityResponseDTO responseDto = objectMapper.readValue(response.body(), CapacityResponseDTO.class);
                return Optional.of(responseDto.getCapacity());
            } else {
                System.err.println("⚠️ El servidor respondió pero no con 200 OK.");
                return Optional.empty();
            }

        } catch (Exception ex) {
            // --- LOG DE ERROR CRÍTICO (Aquí saldrá el motivo real del fallo) ---
            System.err.println("❌ ERROR DE CONEXIÓN CON PLASSB:");
            ex.printStackTrace(); // <--- ESTO ES LO QUE NECESITAMOS VER
            return Optional.empty();
        }
    }

    // AQUI ESTÁ LO QUE QUERÍAS: Usamos los parámetros sueltos
    @Override
    public boolean notifyAssignment(long plantId, String date, int totalDumpsters, float totalWaste) {
        try {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("plantId", plantId);
            jsonMap.put("date", date);
            jsonMap.put("totalDumpsters", totalDumpsters);
            jsonMap.put("totalWaste", totalWaste);

            String jsonBody = objectMapper.writeValueAsString(jsonMap);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PLAN_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200 || response.statusCode() == 201;

        } catch (Exception e) {
            System.err.println("Error enviando plan: " + e.getMessage());
            return false;
        }
    }
}