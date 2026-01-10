package es.deusto.ingenieria.sd.Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// ¡ADIÓS JACKSON! No necesitamos imports externos.

public class ContSocketServer {

    private final int port;

    public ContSocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("--- Servidor ContSocket (Modo Manual) Iniciado en puerto " + port + " ---");
            int clientCount = 0;

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                System.out.println(" - Nueva conexion aceptada. Cliente #" + clientCount);

                final int clientNumber = clientCount;
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
        System.out.println("   > Recibido Cliente #" + clientNumber + ": " + requestJson);

        // --- LÓGICA DE DECISIÓN ---
        // Miramos si el JSON tiene la palabra "ASSIGNMENT" o campos de basura
        if (requestJson.contains("ASSIGNMENT") || requestJson.contains("totalWaste")) {
            
            // === CASO 1: ES UNA NOTIFICACIÓN DE BASURA ===
            System.out.println("   🔔 ¡NOTIFICACIÓN RECIBIDA!");
            
            long plantId = extractLong(requestJson, "plantId");
            double waste = extractDouble(requestJson, "totalWaste");
            long dumpsters = extractLong(requestJson, "totalDumpsters");

            System.out.println("     - Planta ID: " + plantId);
            System.out.println("     - Contenedores: " + dumpsters);
            System.out.println("     - Basura Total: " + waste + " kg");

            // Respondemos OK
            out.writeUTF("{\"status\":\"OK\", \"message\":\"Received\"}");

        } else {
            
            // === CASO 2: ES UNA CONSULTA DE CAPACIDAD (Lo que ya tenías) ===
            System.out.println("   🔍 Consulta de capacidad detectada.");
            long plantId = extractLong(requestJson, "plantId");
            // Aquí tu lógica de BBDD simulada...
            double capacity = 115.5; // (O tu lógica real de búsqueda)

            String responseJson = "{\"plantId\": " + plantId + ", \"capacity\": " + capacity + ", \"status\": \"OK\"}";
            out.writeUTF(responseJson);
        }

    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
    } finally {
        try { clientSocket.close(); } catch (IOException ignored) {}
    }
}

 // --- MÉTODOS AUXILIARES PARA NO USAR LIBRERÍAS ---
private double extractDouble(String json, String key) {
    try {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return 0.0;
        
        start += search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        
        String value = json.substring(start, end).trim();
        return Double.parseDouble(value);
    } catch (Exception e) { return 0.0; }
}
    
    // Saca un número de un JSON simple
    private long extractLong(String json, String key) {
        try {
            String search = "\"" + key + "\":";
            int start = json.indexOf(search);
            if (start == -1) return -1;
            
            start += search.length(); // Nos ponemos después de los dos puntos
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            
            String value = json.substring(start, end).trim();
            return Long.parseLong(value);
        } catch (Exception e) { return -1; }
    }

    // Saca un texto de un JSON simple
    private String extractString(String json, String key) {
        try {
            String search = "\"" + key + "\":\""; // Busca "key":"
            int start = json.indexOf(search);
            if (start == -1) return "unknown";
            
            start += search.length();
            int end = json.indexOf("\"", start); // Busca la comilla de cierre
            
            return json.substring(start, end);
        } catch (Exception e) { return "unknown"; }
    }

    public static void main(String[] args) {
        new ContSocketServer(8080).start();
    }
}