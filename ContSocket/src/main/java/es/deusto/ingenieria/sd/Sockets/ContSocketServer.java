package es.deusto.ingenieria.sd.Sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ContSocketServer {

    private static int numClients = 0;
    
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) {
        int serverPort = DEFAULT_PORT;

        if (args.length > 0) {
            serverPort = Integer.parseInt(args[0]);
        }

        try (ServerSocket tcpServerSocket = new ServerSocket(serverPort)) {
            System.out.println("--- Servidor ContSocket Iniciado en puerto " + serverPort + " ---");

            while (true) {
                // Espera conexión del cliente (tu Spring Boot)
                Socket clientSocket = tcpServerSocket.accept();
                // Lanza el hilo ContService que hemos modificado arriba
                new ContService(clientSocket);
                System.out.println(" - Nueva conexion aceptada. Cliente #" + (++numClients));
            }
        } catch (IOException e) {
            System.err.println("# Error en el servidor: " + e.getMessage());
        }
    }
}
