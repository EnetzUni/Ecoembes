package es.deusto.sd.ecoembes.client.proxies;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.DeserializationFeature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.client.model.*;

public class HttpServiceProxy implements IEcoembesServiceProxy {

    private static final String BASE_URL = "http://localhost:8082"; 
    
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override

    public Map<String, Object> login(Credentials credentials) {
        try {
            String body = objectMapper.writeValueAsString(credentials);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                // Truco: Convertimos el JSON {"token":"...", "employeeId":1} a un Mapa Java
                return objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>(){});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si falla
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
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ecoembes/dumpsters"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                List<Dumpster> dumpsters = new ArrayList<>();

                for (JsonNode node : root) {
                    long id = node.get("id").asLong();
                    String location = node.get("location").asText();
                    float maxCapacity = (float) node.get("maxCapacity").asDouble();

                    int containerCount = node.has("containerCount") ? node.get("containerCount").asInt() : 0;
                    float fillLevel = node.has("fillLevel") ? (float) node.get("fillLevel").asDouble() : 0f;

                    dumpsters.add(new Dumpster(id, location, maxCapacity, containerCount, fillLevel));
                }
                return dumpsters;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public Dumpster createDumpster(Dumpster dumpster, String token) {
        try {
            // Usamos TU clase Dumpster directamente. Jackson se encarga.
            String json = objectMapper.writeValueAsString(dumpster);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ecoembes/dumpsters"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                // ¡AQUÍ ESTÁ LA MAGIA!
                // Leemos la respuesta del server y la convertimos de vuelta a tu clase Dumpster
                return objectMapper.readValue(response.body(), Dumpster.class);
            } else {
                throw new RuntimeException("Error creando Dumpster (" + response.statusCode() + "): " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null; // O lanza excepción si prefieres
        }
    }

    @Override
    public List<RecyclingPlant> getPlants(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ecoembes/plants"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                List<RecyclingPlant> plants = new ArrayList<>();

                for (JsonNode node : root) {
                    long id = node.get("id").asLong();
                    String name = node.get("name").asText();
                    String location = node.get("location").asText();
                    float capacity = (float) node.get("capacity").asDouble();

                    plants.add(new RecyclingPlant(id, name, location, capacity));
                }

                return plants;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return List.of();
    }

    @Override
    public float getPlantCapacity(long plantId, String date, String token) {
        try {
            String url = BASE_URL + "/api/external/capacity?plantId=" + plantId + "&date=" + date;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JsonNode node = objectMapper.readTree(response.body());
                if (node.has("capacity")) {
                    return (float) node.get("capacity").asDouble();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        return -1f;
    }

    @Override
    public void createAssignment(long plantId, List<Long> dumpsterIds, Long employeeId, String token) {
        try {
            Map<String, Object> assignmentData = new HashMap<>();
            
            // 1. Coincide con: public Long recyclingPlantId;
            assignmentData.put("recyclingPlantId", plantId); 
            
            // 2. Coincide con: public List<Long> dumpsterIds;
            assignmentData.put("dumpsterIds", dumpsterIds);
            
            // 3. Coincide con: public Long employeeId;
            assignmentData.put("employeeId", employeeId); 

            String json = objectMapper.writeValueAsString(assignmentData);

            // DEBUG: Verás que ahora sí envía los nombres correctos
            System.out.println("JSON enviado: " + json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ecoembes/assignments"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200 && response.statusCode() != 201) {
                System.err.println("Error Server: " + response.body());
                throw new RuntimeException("Error (" + response.statusCode() + "): " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error de conexión al crear asignación");
        }
    }

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
        return List.of();
    }

    @Override
    public List<Assignment> getAssignments(String token) {
        return sendGetRequest(BASE_URL + "/ecoembes/assignments", token, new TypeReference<List<Assignment>>() {});
    }
}