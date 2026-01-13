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

    private static final String BASE_URL = "http://localhost:8082"; 
    
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
                return response.body(); // Returns the token
            } else {
                throw new RuntimeException("Login failed: " + response.statusCode());
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
            String jsonBody = objectMapper.writeValueAsString(dumpster);
        
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ecoembes/dumpsters"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
        
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return objectMapper.readValue(response.body(), Dumpster.class);
            } else {
                throw new RuntimeException("Error creating dumpster: " + response.statusCode() + " - " + response.body());
            }
        
        } catch (Exception e) {
            throw new RuntimeException("Error in communication while creating dumpster", e);
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
    public void createAssignment(long plantId, List<Long> dumpsterIds, String token) {
        try {
            Map<String, Object> payload = new HashMap<>();
            
            // --- FIXED: Use "plantId" instead of "recyclingPlantId" ---
            payload.put("plantId", plantId); 
            
            payload.put("date", LocalDate.now().toString());
            
            // --- FIXED: Use "dumpsterIds" (This was already partly fixed in your snippet) ---
            payload.put("dumpsterIds", new ArrayList<>(dumpsterIds)); 

            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ecoembes/assignments"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200 && response.statusCode() != 201) {
                throw new RuntimeException("Error creating assignment (" + response.statusCode() + "): " + response.body());
            }
        } catch (Exception e) { throw new RuntimeException(e); }
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