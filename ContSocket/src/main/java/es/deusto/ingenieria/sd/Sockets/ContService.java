package es.deusto.ingenieria.sd.Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ContService extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket tcpSocket;

    // El delimitador que tu cliente Spring Boot espera
    private static final String DELIMITER = "#";
    private static final String PLANT_NAME = "ContSocket Ltd.";

    public ContService(Socket socket) {
        try {
            this.tcpSocket = socket;
            // CAMBIO CLAVE 1: Usamos DataStream en lugar de BufferedReader/PrintWriter
            // para que sea compatible con los writeUTF/readUTF de tu cliente.
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.err.println("# ContService - Error de IO: " + e.getMessage());
        }
    }

    public void run() {
        try {
            // Leemos la petición (ej: "GET_ASSIGNMENT#ContSocket Ltd.")
            String request = this.in.readUTF();
            System.out.println("   - Recibido: " + request);

            // Procesamos la lógica
            String response = this.processRequest(request);

            // CAMBIO CLAVE 2: Enviamos la respuesta en formato String simple con #, NO JSON.
            this.out.writeUTF(response);
            System.out.println("   - Enviado: " + response);

        } catch (EOFException e) {
            System.err.println("   # Cliente desconectado.");
        } catch (IOException e) {
            System.err.println("   # Error de IO: " + e.getMessage());
        } finally {
            try {
                tcpSocket.close();
            } catch (IOException e) {
                System.err.println("   # Error cerrando socket: " + e.getMessage());
            }
        }
    }

    private String processRequest(String request) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(request, DELIMITER);
            
            // Validamos que venga comando y planta
            if (tokenizer.countTokens() < 2) {
                return "ERROR" + DELIMITER + "Peticion mal formada";
            }
            
            String command = tokenizer.nextToken();
            String plantId = tokenizer.nextToken();

            // Lógica simple para responder a tu cliente
            // CORRECCIÓN: Ahora validamos también que el plantId sea el correcto
            if ("GET_ASSIGNMENT".equals(command) && PLANT_NAME.equals(plantId)) {
                // Simulamos capacidad (ej. 150 toneladas) y disponibilidad (true)
                // Formato esperado: OK#PlantaID#Capacidad#Aceptado
                return "OK" + DELIMITER + PLANT_NAME + DELIMITER + "150.0" + DELIMITER + "true";
            } else {
                return "ERROR" + DELIMITER + "Comando desconocido o ID de planta incorrecto";
            }
        } catch (Exception e) {
            return "ERROR" + DELIMITER + "Error procesando";
        }
    }
}
