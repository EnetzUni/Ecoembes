package es.deusto.ingenieria.sd.Sockets;

import java.net.ServerSocket;
import java.net.Socket;

public class ContSocketServer {

    public static void main(String[] args) {

        int port = 5000; // Debe coincidir con tu Ecoembes HOST/PORT

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("[ContServer] Running on port " + port);

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("[ContServer] New connection");
                new ContService(client);
            }

        } catch (Exception e) {
            System.err.println("[ContServer] Fatal error: " + e.getMessage());
        }
    }
}

