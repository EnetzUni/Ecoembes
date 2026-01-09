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

    // URL directa a tu servidor PlasSB (Puerto 8081)
    // URL 1: Para consultar si cabe basura (antes se llamaba API_URL)
    private final String API_URL = "http://localhost:8081/api/capacities/check";
    
    // URL 2: Para enviar el plan diario (la nueva)
    private final String PLAN_URL = "http://localhost:8081/api/plans";
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // 1. Constructor igual que en CurrencyServiceGateway
    public RestRecyclingGateway() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Optional<Float> getCapacity(CapacityRequestDTO requestDto) {
        try {
            // 2. Convertimos el objeto Java (DTO) a texto JSON
            // (Esto no estaba en Currency porque allí enviaban parámetros en la URL)
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // 3. Crear la Request (Estilo Currency, pero con POST)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json") // Importante avisar que enviamos JSON
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 4. Enviar y obtener respuesta (Idéntico a Currency)
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 5. Validar respuesta (Idéntico a Currency)
            if (response.statusCode() == 200) {
                // Parseamos el JSON de vuelta al DTO de respuesta
                CapacityResponseDTO responseDto = objectMapper.readValue(response.body(), CapacityResponseDTO.class);
                
                // Devolvemos el dato que nos interesa (la capacidad)
                return Optional.of(responseDto.getCapacity());
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            // Gestión de errores igual que el template
            System.err.println("Error en Gateway: " + ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean sendDailyPlan(long plantId, String date, int totalDumpsters, float totalWaste) {
        try {
            // TRUCO: Creamos un Mapa para simular el objeto JSON
            Map<String, Object> jsonMap = new HashMap<>();
            
            // Las claves ("keys") deben ser EXACTAMENTE las que espera PlasSB
            jsonMap.put("plantId", plantId);
            jsonMap.put("date", date);
            jsonMap.put("totalDumpsters", totalDumpsters);
            jsonMap.put("totalWaste", totalWaste);

            // Jackson convierte el Mapa a un String JSON igual que si fuera un DTO
            String jsonBody = objectMapper.writeValueAsString(jsonMap);

            // El resto es igual...
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