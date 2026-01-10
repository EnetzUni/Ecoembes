package es.deusto.sd.ecoembes.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import es.deusto.sd.ecoembes.dto.CapacityResponseDTO;

@Component
public class SocketRecyclingGateway implements IExternalRecyclingGateway {

    private final String serverIP;
    private final int serverPort;
    private final ObjectMapper mapper = new ObjectMapper();

    public SocketRecyclingGateway() {
        // Configuración por defecto (ajusta si tu server de sockets tiene otra IP/Puerto)
        this.serverIP = "127.0.0.1"; 
        this.serverPort = 8080;     
    }

    @Override
    public Optional<Float> getCapacity(CapacityRequestDTO request) {
        try (Socket socket = new Socket(serverIP, serverPort);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // --- CAMBIO: Ya no convertimos Date a LocalDate ---
            // Como request.getDate() ya es un String "yyyy-MM-dd", lo enviamos directo.
            String messageToSend = "GET#" + request.getPlantId() + "#" + request.getDate();
            
            // Enviamos mensaje al servidor de sockets
            out.writeUTF(messageToSend);

            // Leemos respuesta
            String response = in.readUTF();

            if ("NOT_FOUND".equalsIgnoreCase(response)) {
                return Optional.empty();
            }

            // Convertimos la respuesta JSON a objeto
            CapacityResponseDTO dto = mapper.readValue(response, CapacityResponseDTO.class);

            return Optional.of(dto.getCapacity());

        } catch (IOException e) {
            // Es normal que falle si no tienes el servidor de sockets levantado, 
            // pero al menos no rompe la compilación.
            System.err.println("Socket (No crítico si usas REST): " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean sendDailyPlan(long plantId, String date, int totalDumpsters, float totalWaste) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendDailyPlan'");
    }
}