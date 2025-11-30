package es.deusto.sd.ecoembes.external;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestRecyclingGateway {

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private final String BASE_URL = "http://localhost:8081/api";

    /**
     * Calls PlasSB endpoint:
     * GET /api/capacity?plantId=X&date=YYYY-MM-DD
     */
    public Optional<Float> getDailyCapacity(long plantId, String dateIso) {
        try {
            String url = BASE_URL + "/capacity?plantId=" + plantId + "&date=" + dateIso;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            String body = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            // because the controller returns a JSON number, mapper reads it as Float
            Float capacity = mapper.readValue(body, Float.class);

            return Optional.ofNullable(capacity);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
