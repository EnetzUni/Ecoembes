package es.deusto.sd.ecoembes.external.gateway;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.external.dto.ExternalPlantInfo;

@Component
public class RestRecyclingGateway implements RecyclingGateway {

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private final String PLASSB_URL = "http://localhost:8085/api";

    @Override
    public Optional<ExternalPlantInfo> getDailyCapacity() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(PLASSB_URL + "/capacity"))
                .GET()
                .build();

            String body = client.send(req, HttpResponse.BodyHandlers.ofString())
                                .body();

            ExternalPlantInfo info = mapper.readValue(body, ExternalPlantInfo.class);
            return Optional.of(info);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean sendAssignment(long dumpsterId, long numContainers) {
        try {
            String json = String.format("""
            {"dumpsterId": %d, "numContainers": %d}
            """, dumpsterId, numContainers);

            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(PLASSB_URL + "/assignment"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            return resp.statusCode() == 200;

        } catch (Exception ex) {
            return false;
        }
    }
}
