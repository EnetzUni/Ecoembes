package es.deusto.sd.ecoembes.client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JOptionPane;

import es.deusto.sd.ecoembes.client.model.Credentials;
import es.deusto.sd.ecoembes.client.proxies.HttpServiceProxy;
import es.deusto.sd.ecoembes.client.proxies.IEcoembesServiceProxy;
import es.deusto.sd.ecoembes.client.util.GuiUtils; // Your utils package
import es.deusto.sd.ecoembes.client.view.LoginView;
import es.deusto.sd.ecoembes.client.view.MenuView; // Importing the next screen

public class LoginController implements ActionListener {

    private IEcoembesServiceProxy serviceProxy = new HttpServiceProxy();
    private LoginView view;
    private String token;
    private Long loggedEmployeeId;

    // Constructor wires the View to this Controller
    public LoginController(LoginView view) {
        this.view = view;
        // Tell the view to call 'actionPerformed' in this class when button is clicked
        this.view.addSubmitListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        // 1. Get data from the view
        String email = view.getLoginEmailString();
        String password = view.getLoginEmailPassword();

        System.out.println("Login attempt for: " + email);

        // 2. Try to login
        boolean success = login(email, password);

        if (success) {
            // --- SUCCESS: SWITCH FRAMES ---
            System.out.println("Login successful! Token: " + token + " | ID: " + loggedEmployeeId);

            // 1. Create the View (The Body)
            MenuView menuView = new MenuView();

            // 2. Create the Controller (The Brain)
            // Passing the view to the controller activates the buttons!
            // AHORA PASAMOS EL ID TAMBIÉN
            new MenuController(menuView, token, loggedEmployeeId);

            // 3. Switch Frames
            GuiUtils.switchFrames(view.getFrame(), menuView.getFrame());
            

        } else {
            // --- FAILURE: SHOW ERROR ---
            System.out.println("Login failed.");
            JOptionPane.showMessageDialog(view.getFrame(), 
                "Login failed. Please check your credentials.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean login(String email, String password) {
        try {
            // El proxy devuelve ahora un Map<String, Object>
            Map<String, Object> response = serviceProxy.login(new Credentials(email, password));
            
            if (response != null && response.containsKey("token")) {
                // 1. Guardamos el Token
                this.token = (String) response.get("token");
                
                // 2. Guardamos el ID (Usamos Number para evitar errores de cast Integer/Long)
                if (response.containsKey("employeeId")) {
                    Number num = (Number) response.get("employeeId");
                    this.loggedEmployeeId = num.longValue();
                }
                
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void logout() {
        if (token != null) serviceProxy.logout(token);
    }
}
