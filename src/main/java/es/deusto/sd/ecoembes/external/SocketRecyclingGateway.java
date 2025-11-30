package es.deusto.sd.ecoembes.external;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SocketRecyclingGateway implements IExternalRecyclingGateway {

    private final String HOST = "contsocketserver.com"; 
    private final int PORT = 5000;

    private final ObjectMapper mapper = new ObjectMapper();

    private String sendAndReceive(String message) throws IOException {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            writer.println(message);
            return reader.readLine();
        }
    }

    @Override
    public Optional<ExternalPlantInfo> getPlantInfo(long plantId) {
        try {
            String req = "GET_PLANT " + plantId;
            String resp = sendAndReceive(req);

            ExternalPlantInfo info = mapper.readValue(resp, ExternalPlantInfo.class);
            return Optional.of(info);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean sendAssignment(ExternalAssignmentDTO dto) {
        try {
            String json = mapper.writeValueAsString(dto);
            String resp = sendAndReceive("SEND_ASSIGNMENT " + json);

            return "OK".equals(resp);

        } catch (Exception e) {
            return false;
        }
    }
}
