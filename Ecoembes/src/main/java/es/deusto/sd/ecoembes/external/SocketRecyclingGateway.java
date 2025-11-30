package es.deusto.sd.ecoembes.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketRecyclingGateway implements ExternalGateway {

    private String serverIP;
    private int serverPort;
    private static final String DELIMITER = "#";

    public SocketRecyclingGateway(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public ExternalAssignmentDTO getAssignmentInfo(String plantId) {
        String request = "GET_ASSIGNMENT" + DELIMITER + plantId;
        String response = null;

        try (Socket socket = new Socket(serverIP, serverPort);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            out.writeUTF(request);
            System.out.println("Enviado: " + request);

            response = in.readUTF();
            System.out.println("Recibido: " + response);

        } catch (IOException e) {
            System.err.println("SocketRecyclingGateway ERROR: " + e.getMessage());
            return null;
        }

        // Ejemplo respuesta: OK#plantA#45.2#YES
        String[] parts = response.split(DELIMITER);

        if (!parts[0].equals("OK")) {
            return null;
        }

        ExternalAssignmentDTO dto = new ExternalAssignmentDTO();
        dto.setPlantId(parts[1]);
        dto.setWeight(Double.parseDouble(parts[2]));
        dto.setAccepted(Boolean.parseBoolean(parts[3]));

        return dto;
    }
}
