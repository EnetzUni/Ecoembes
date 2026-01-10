package es.deusto.sd.ecoembes.client;

import es.deusto.sd.ecoembes.client.view.LoginView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginWindow = new LoginView();
            loginWindow.show();
        });
    }
}