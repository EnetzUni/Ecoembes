package es.deusto.sd.ecoembes.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import es.deusto.sd.ecoembes.dto.CapacityResponseDTO;
import org.springframework.stereotype.Component; // <--- IMPORTANTE

@Component // <--- ESTO ES LO QUE TE FALTA
public class SocketRecyclingGateway implements IExternalRecyclingGateway {

    private final String serverIP;
    private final int serverPort;
    private final ObjectMapper mapper = new ObjectMapper();

    public SocketRecyclingGateway(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    // -----------------------------
    // Implementación de la interfaz
    // -----------------------------
    @Override
    public Optional<Float> getCapacity(CapacityRequestDTO request) {
        try (Socket socket = new Socket(serverIP, serverPort);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Convertimos la fecha a LocalDate (yyyy-MM-dd)
            LocalDate localDate = request.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // Enviamos la consulta al servidor de sockets
            out.writeUTF("GET#" + request.getPlantId() + "#" + localDate.toString());

            // Leemos respuesta
            String response = in.readUTF();

            if ("NOT_FOUND".equalsIgnoreCase(response)) {
                return Optional.empty();
            }

            // Convertimos JSON a CapacityResponseDTO
            CapacityResponseDTO dto = mapper.readValue(response, CapacityResponseDTO.class);

            return Optional.of(dto.getCapacity());

        } catch (IOException e) {
            System.err.println("Socket ERROR getCapacity: " + e.getMessage());
            return Optional.empty();
        }
    }

    // -----------------------------
    // Método opcional para enviar capacidad
    // -----------------------------
    public boolean sendCapacity(CapacityResponseDTO dto) {
        try (Socket socket = new Socket(serverIP, serverPort);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            String json = mapper.writeValueAsString(dto);
            out.writeUTF("SEND#" + json);

            String response = in.readUTF();
            return "OK".equalsIgnoreCase(response);

        } catch (IOException e) {
            System.err.println("Socket ERROR sendCapacity: " + e.getMessage());
            return false;
        }
    }
}
