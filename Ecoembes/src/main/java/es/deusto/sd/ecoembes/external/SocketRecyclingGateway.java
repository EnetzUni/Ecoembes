package es.deusto.sd.ecoembes.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.plassb.dto.CapacityRequestDTO;
import es.deusto.sd.plassb.dto.CapacityResponseDTO;

public class SocketRecyclingGateway {

    private final String serverIP;
    private final int serverPort;
    private final ObjectMapper mapper = new ObjectMapper();

    public SocketRecyclingGateway(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    // -----------------------------
    // Enviar capacidad al servidor
    // -----------------------------
    public boolean sendCapacity(CapacityResponseDTO dto) {
        try (Socket socket = new Socket(serverIP, serverPort);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Convertimos DTO a JSON y enviamos con prefijo SEND#
            String json = mapper.writeValueAsString(dto);
            out.writeUTF("SEND#" + json);

            // Leemos respuesta
            String response = in.readUTF();
            return "OK".equalsIgnoreCase(response);

        } catch (IOException e) {
            System.err.println("Socket ERROR sendCapacity: " + e.getMessage());
            return false;
        }
    }

    // -----------------------------
    // Consultar capacidad del servidor
    // -----------------------------
    public Optional<CapacityResponseDTO> getCapacity(long plantId, Date date) {
        try (Socket socket = new Socket(serverIP, serverPort);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Convertimos Date a LocalDate para el formato yyyy-MM-dd
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Enviamos GET#plantId#fecha
            out.writeUTF("GET#" + plantId + "#" + localDate.toString());

            // Leemos respuesta
            String response = in.readUTF();

            if ("NOT_FOUND".equalsIgnoreCase(response)) {
                return Optional.empty();
            }

            // Convertimos JSON a DTO
            CapacityResponseDTO dto = mapper.readValue(response, CapacityResponseDTO.class);
            return Optional.of(dto);

        } catch (IOException e) {
            System.err.println("Socket ERROR getCapacity: " + e.getMessage());
            return Optional.empty();
        }
    }
}
