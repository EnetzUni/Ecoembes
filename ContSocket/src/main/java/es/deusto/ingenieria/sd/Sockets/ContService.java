package es.deusto.ingenieria.sd.Sockets;

import java.io.*;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ContService extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final ObjectMapper mapper = new ObjectMapper();

    public ContService(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        start();
    }

    @Override
    public void run() {
        try {
            String msg = in.readLine();

            if (msg == null) return;

            System.out.println("[ContServer] Received: " + msg);

            String response = handleMessage(msg);

            out.println(response);
            System.out.println("[ContServer] Sent: " + response);

        } catch (Exception e) {
            System.err.println("[ContServer] Error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private String handleMessage(String msg) {

        try {
            if (msg.startsWith("GET_PLANT ")) {
                long id = Long.parseLong(msg.split(" ")[1]);
                ExternalPlantInfo plant = MemoryStorage.getPlant(id);
                return (plant != null) ? mapper.writeValueAsString(plant) : "ERR";
            }

            if (msg.startsWith("SEND_ASSIGNMENT ")) {
                String json = msg.substring("SEND_ASSIGNMENT ".length());
                ExternalAssignmentDTO dto = mapper.readValue(json, ExternalAssignmentDTO.class);
                MemoryStorage.saveAssignment(dto);
                return "OK";
            }

            return "ERR";

        } catch (Exception e) {
            return "ERR";
        }
    }
}

