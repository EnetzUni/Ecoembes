package es.deusto.sd.ecoembes.client.proxies;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.client.model.*;

public class HttpServiceProxy implements IEcoembesServiceProxy {

    private static final String BASE_URL = "http://localhost:8081"; 
    
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String login(Credentials credentials) {
        try {
            String body = objectMapper.writeValueAsString(credentials);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return response.body(); // Retorna el token
            } else {
                throw new RuntimeException("Login fallido: " + response.statusCode());
            }
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    @Override
    public void logout(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/logout"))
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString(token))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public List<Dumpster> getDumpsters(String token) {
        // Asumo que tienes un método GET /ecoembes/dumpsters en EcoembesController
        // Si no lo tienes, usa /ecoembes/dumpsters (si lo creaste) o ajusta la ruta.
        return sendGetRequest(BASE_URL + "/ecoembes/dumpsters", token, new TypeReference<List<Dumpster>>() {});
    }

    @Override
    public List<RecyclingPlant> getPlants(String token) {
        // Ruta vista en EcoembesController: @GetMapping("/plants") -> /ecoembes/plants
        return sendGetRequest(BASE_URL + "/ecoembes/plants", token, new TypeReference<List<RecyclingPlant>>() {});
    }

    @Override
    public float getPlantCapacity(long plantId, String date, String token) {
        try {
            // CAMBIO: Usamos la variable 'date' del argumento directamente en la URL
            String url = BASE_URL + "/api/external/capacity?plantId=" + plantId + "&date=" + date;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                // Leemos el JSON {"capacity": 123.0, ...}
                JsonNode node = objectMapper.readTree(response.body());
                if (node.has("capacity")) {
                    return (float) node.get("capacity").asDouble();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        return -1f; // Retorno de error
    }

    @Override
    public void createAssignment(long plantId, List<Long> dumpsterIds, String token) {
        try {
            // TRUCO: Construimos un JSON que parezca un AssignmentDTO.
            // El servidor espera: { "recyclingPlantId": X, "dumpsters": [ { "id": Y }, { "id": Z } ], "date": "..." }
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("recyclingPlantId", plantId);
            payload.put("date", LocalDate.now().toString()); // Fecha hoy
            
            // Convertimos la lista de IDs a una lista de objetos básicos para que Jackson cree [{"id":1}, {"id":2}]
            List<Map<String, Long>> dumpstersList = new ArrayList<>();
            for (Long id : dumpsterIds) {
                Map<String, Long> d = new HashMap<>();
                d.put("id", id);
                dumpstersList.add(d);
            }
            payload.put("dumpsters", dumpstersList);

            String json = objectMapper.writeValueAsString(payload);
            
            // Ruta asumida en EcoembesController: POST /ecoembes/assignments
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ecoembes/assignments"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200 && response.statusCode() != 201) {
                throw new RuntimeException("Error creando asignación (" + response.statusCode() + "): " + response.body());
            }
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    // Método auxiliar genérico para GET
    private <T> List<T> sendGetRequest(String url, String token, TypeReference<List<T>> typeRef) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), typeRef);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return List.of(); // Retorna lista vacía en caso de error
    }
}