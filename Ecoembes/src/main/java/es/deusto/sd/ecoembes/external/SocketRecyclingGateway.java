package es.deusto.sd.ecoembes.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SocketRecyclingGateway implements IExternalRecyclingGateway {

    private String serverIP;
    private int serverPort;
    private static final String DELIMITER = "#";
    
    // Nombre de la planta que espera el servidor de sockets (Hardcodeado o por configuración)
    private static final String TARGET_PLANT_NAME = "ContSocket Ltd.";

    // Inyectamos valores desde application.properties si es necesario, o valores por defecto
    public SocketRecyclingGateway(@Value("${socket.server.ip:127.0.0.1}") String serverIP, 
                                  @Value("${socket.server.port:8081}") int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public Optional<ExternalPlantInfo> getPlantInfo(long plantId) {
        // Protocolo: GET_ASSIGNMENT#NombrePlanta
        String request = "GET_ASSIGNMENT" + DELIMITER + TARGET_PLANT_NAME;
        
        try (Socket socket = new Socket(serverIP, serverPort);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            out.writeUTF(request);
            // Respuesta esperada: OK#Nombre#Capacidad#Acepta
            String response = in.readUTF(); 
            
            String[] parts = response.split(DELIMITER);
            if (parts.length >= 4 && "OK".equals(parts[0])) {
                // Mapeamos la respuesta del socket al objeto ExternalPlantInfo
                ExternalPlantInfo info = new ExternalPlantInfo();
                info.id = plantId; // Mantenemos el ID que nos pidió el sistema
                info.name = parts[1]; // Nombre devuelto por el socket
                info.availableCapacity = Float.parseFloat(parts[2]); // Capacidad parseada a float
                
                return Optional.of(info);
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error en SocketRecyclingGateway: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    @Override
    public boolean sendAssignment(ExternalAssignmentDTO dto) {
        // IMPORTANTE: El servidor de sockets actual (ContSocketServer) 
        // SOLO soporta consultar capacidad (GET_ASSIGNMENT). 
        // No tiene implementado recibir asignaciones. 
        // Devolvemos false o implementamos la lógica si el servidor cambia.
        System.err.println("Operación sendAssignment no soportada por ContSocketServer actual.");
        return false;
    }
}