package es.deusto.ingenieria.sd.Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.deusto.ingenieria.sd.Sockets.CapacityRequestDTO;
import es.deusto.ingenieria.sd.Sockets.CapacityResponseDTO;

public class ContSocketServer {

    private final int port;
    private final ObjectMapper mapper = new ObjectMapper();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ContSocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("--- Servidor ContSocket Iniciado en puerto " + port + " ---");
            int clientCount = 0;

            while (true) {
        Socket clientSocket = serverSocket.accept();
        clientCount++;
        System.out.println(" - Nueva conexion aceptada. Cliente #" + clientCount);

        final int clientNumber = clientCount; // copia final para la lambda
        new Thread(() -> handleClient(clientSocket, clientNumber)).start();
}


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket, int clientNumber) {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            String requestJson = in.readUTF();
            CapacityRequestDTO request = mapper.readValue(requestJson, CapacityRequestDTO.class);

            CapacityResponseDTO response = MemoryStorage.getCapacity(request.getPlantId(), request.getDate());

            String responseJson;
            if (response != null) {
                responseJson = mapper.writeValueAsString(response);
            } else {
                responseJson = "{\"error\":\"No se encontro capacidad\"}";
            }

            out.writeUTF(responseJson);

        } catch (IOException e) {
            System.err.println(" - Error Cliente #" + clientNumber + ": " + e.getMessage());
        } finally {
            try { clientSocket.close(); } catch (IOException ignored) {}
            System.out.println("   # Cliente #" + clientNumber + " desconectado.");
        }
    }

    public static void main(String[] args) {
        new ContSocketServer(8080).start();
    }
}