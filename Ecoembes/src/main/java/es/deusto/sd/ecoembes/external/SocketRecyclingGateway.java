package es.deusto.sd.ecoembes.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.ecoembes.dto.CapacityRequestDTO;
import es.deusto.sd.ecoembes.dto.CapacityResponseDTO; // Asegúrate de que este DTO existe en Ecoembes también
import es.deusto.sd.ecoembes.entity.Assignment;

@Component
public class SocketRecyclingGateway implements IExternalRecyclingGateway {

    private final String serverIP = "127.0.0.1";
    private final int serverPort = 8080;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Optional<Float> getCapacity(CapacityRequestDTO request) {
        try (Socket socket = new Socket(serverIP, serverPort);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // 1. PREPARAR EL JSON DE PETICIÓN
            // Creamos un Map temporal para convertirlo a JSON.
            // Las claves ("plantId", "date") deben coincidir con CapacityyRequestDTO del servidor
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("plantId", request.getPlantId());
            requestMap.put("date", request.getDate()); // Ya es String (ej: "2025-01-10")

            String jsonRequest = mapper.writeValueAsString(requestMap);
            
            System.out.println("🔌 [Socket] Enviando: " + jsonRequest);
            
            // 2. ENVIAR AL SERVIDOR
            out.writeUTF(jsonRequest);

            // 3. RECIBIR RESPUESTA
            String jsonResponse = in.readUTF();
            System.out.println("🔌 [Socket] Recibido: " + jsonResponse);

            // 4. PROCESAR RESPUESTA
            // Si el servidor devuelve un JSON con error o null...
            if (jsonResponse.contains("error") || jsonResponse.contains("No se encontro")) {
                return Optional.empty();
            }

            // Mapeamos el JSON recibido a nuestro DTO local
            // OJO: Ecoembes debe tener una clase CapacityResponseDTO compatible o usar un Map
            CapacityResponseDTO responseDto = mapper.readValue(jsonResponse, CapacityResponseDTO.class);

            return Optional.of(responseDto.getCapacity());

        } catch (IOException e) {
            System.err.println("❌ Error en conexión Socket: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
public boolean notifyAssignment(long plantId, String date, int totalDumpsters, float totalWaste) {
    // Definimos IP y Puerto (asegúrate que coinciden con tu configuración final: 8080 o 9000)
    // Según tu última config:
    String ip = "127.0.0.1"; 
    int port = 8080; 

    try (Socket socket = new Socket(ip, port);
         DataOutputStream out = new DataOutputStream(socket.getOutputStream());
         DataInputStream in = new DataInputStream(socket.getInputStream())) {

        // 1. PREPARAR EL JSON
        // Añadimos un campo "type" para que el servidor sepa qué hacer
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("type", "ASSIGNMENT"); // <--- CLAVE IMPORTANTE
        requestMap.put("plantId", plantId);
        requestMap.put("date", date);
        requestMap.put("totalDumpsters", totalDumpsters);
        requestMap.put("totalWaste", totalWaste);

        String jsonRequest = mapper.writeValueAsString(requestMap);
        
        System.out.println("🔌 [Socket OUT] Notificando asignación: " + jsonRequest);

        // 2. ENVIAR
        out.writeUTF(jsonRequest);

        // 3. RECIBIR CONFIRMACIÓN (Esperamos un "OK" o similar)
        String response = in.readUTF();
        System.out.println("🔌 [Socket IN] Respuesta: " + response);

        return response.contains("OK");

    } catch (IOException e) {
        System.err.println("❌ Error notificando al Socket: " + e.getMessage());
        return false;
    }
}
}