package es.deusto.sd.ecoembes.client;

import es.deusto.sd.ecoembes.client.controller.LoginController; // Import Controller
import es.deusto.sd.ecoembes.client.view.LoginView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Create the View
            LoginView loginWindow = new LoginView();

            // 2. Create the Controller (Connects View <-> Logic)
            new LoginController(loginWindow);

            // 3. Show the View
            loginWindow.show();
        });
    }
}