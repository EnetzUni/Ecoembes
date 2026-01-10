package es.deusto.ingenieria.sd.Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ContSocketServer {

    private final int port;
    
    // 💾 1. LA BASE DE DATOS EN MEMORIA (Lista compartida)
    private static final List<String> assignmentsDb = new ArrayList<>();

    public ContSocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("--- Servidor ContSocket (Con Memoria Visible) Iniciado en puerto " + port + " ---");
            int clientCount = 0;

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;
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

            // Leemos lo que envía el cliente
            String requestJson = in.readUTF(); 
            System.out.println("   > [Cliente #" + clientNumber + "] Envio: " + requestJson);

            // --- TRUCO 1: COMANDO DE ESPÍA ---
            // Si enviamos la palabra "ADMIN_REPORT", el servidor nos chiva todo lo que tiene
            if (requestJson.equals("ADMIN_REPORT")) {
                String report;
                synchronized (assignmentsDb) {
                    report = "--- MEMORIA ACTUAL ---\n" + assignmentsDb.toString();
                }
                out.writeUTF(report);
                System.out.println("   🕵️ [ADMIN] Reporte de memoria enviado.");
                return; // Terminamos aquí
            }

            // --- LÓGICA NORMAL ---
            // Si el mensaje parece una asignación (tiene basura), lo guardamos
            if (requestJson.contains("totalWaste") || requestJson.contains("ASSIGNMENT")) {
                
                synchronized (assignmentsDb) {
                    assignmentsDb.add(requestJson);
                    
                    // --- TRUCO 2: LOG VISUAL INMEDIATO ---
                    // Cada vez que guardamos, imprimimos el tamaño y el último dato
                    System.out.println("   💾 ¡DATO GUARDADO EN RAM!");
                    System.out.println("      Total registros almacenados: " + assignmentsDb.size());
                    System.out.println("      Último registro: " + requestJson);
                }
                
                out.writeUTF("{\"status\":\"OK\", \"msg\":\"Saved in RAM\"}");

            } else {
                // Si no, es una consulta de capacidad normal
                long plantId = extractLong(requestJson, "plantId");
                
                // Lógica dummy para devolver capacidad
                float capacity = (plantId == 1) ? 5000.0f : 0f;
                String responseJson = "{\"plantId\":" + plantId + ", \"capacity\":" + capacity + "}";
                
                out.writeUTF(responseJson);
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try { clientSocket.close(); } catch (IOException ignored) {}
        }
    }

    // Helpers para sacar datos sin Jackson
    private long extractLong(String json, String key) {
        try {
            String search = "\"" + key + "\":";
            int start = json.indexOf(search);
            if (start == -1) return -1;
            start += search.length();
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return Long.parseLong(json.substring(start, end).trim());
        } catch (Exception e) { return -1; }
    }

    public static void main(String[] args) {
        new ContSocketServer(8080).start();
    }
}