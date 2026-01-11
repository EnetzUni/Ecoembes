package es.deusto.sd.ecoembes.client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    // Constructor wires the View to this Controller
    public LoginController(LoginView view) {
        this.view = view;
        // Tell the view to call 'actionPerformed' in this class when button is clicked
        this.view.addSubmitListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 1. Get data from the view
        String email = view.getLoginEmailString();
        String password = view.getLoginEmailPassword();

        System.out.println("Login attempt for: " + email);

        // 2. Try to login
        boolean success = login(email, password);

        if (success) {
            // --- SUCCESS: SWITCH FRAMES ---
            System.out.println("Login successful! Token: " + token);

            // Create the new Menu View
            MenuView menuView = new MenuView();
            new MenuController(menuView, token); //q si no los botones no me van

            // Switch using your GuiUtils
            // Note: Ensure MenuView also has a public getFrame() method!
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
            // Only returns a token if credentials are correct
            this.token = serviceProxy.login(new Credentials(email, password));
            return this.token != null && !this.token.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        if (token != null) serviceProxy.logout(token);
    }
}
