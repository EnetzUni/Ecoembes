package es.deusto.sd.ecoembes.client.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginView {
    private JFrame frame;
    private JButton loginButton;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginView() {
        frame = new JFrame("Ecoembes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- 1. ICON SETUP ---
        URL iconURL = getClass().getResource("/EcoembesLogo1080x1080.png");
        if (iconURL != null) {
            frame.setIconImage(new ImageIcon(iconURL).getImage());
        } else {
            ImageIcon fileIcon = new ImageIcon("EcoembesClient/src/resources/images/EcoembesLogo1080x1080.png");
            frame.setIconImage(fileIcon.getImage());
        }

        // --- WINDOW SETTINGS ---
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(new GridBagLayout()); 

        // --- 2. TOP BANNER IMAGE ---
        JLabel topLogoLabel = new JLabel();
        ImageIcon bannerIcon = null;
        URL bannerURL = getClass().getResource("/EcoembesLogo4320x1080.png");
        
        if (bannerURL != null) bannerIcon = new ImageIcon(bannerURL);
        else bannerIcon = new ImageIcon("EcoembesClient/src/resources/images/EcoembesLogo4320x1080.png");

        if (bannerIcon.getIconWidth() > 0) {
            Image scaledImage = bannerIcon.getImage().getScaledInstance(500, 125, Image.SCALE_SMOOTH);
            topLogoLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            topLogoLabel.setText("Logo Not Found");
        }

        // --- 3. LOGIN PANEL ---
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createEtchedBorder());
        
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setPreferredSize(new Dimension(500, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // -- ROW 0: BIG TITLE --
        JLabel titleLabel = new JLabel("LOGIN PANEL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(10, 15, 15, 15); 
        loginPanel.add(titleLabel, gbc);

        // Reset insets
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridwidth = 1;

        // -- ROW 1: EMAIL --
        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        loginPanel.add(emailField, gbc);

        // -- ROW 2: PASSWORD --
        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        loginPanel.add(passwordField, gbc);

        // -- ROW 3: BUTTON --
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 40));
        
        // --- CHANGED COLOR HERE ---
        loginButton.setBackground(Color.decode("#95c767")); 
        loginButton.setForeground(Color.WHITE);
        loginPanel.add(loginButton, gbc);

        // --- 4. ASSEMBLE FRAME ---
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0; 
        mainGbc.gridy = 0;
        mainGbc.insets = new Insets(0, 0, 20, 0); 
        frame.add(topLogoLabel, mainGbc);

        mainGbc.gridy = 1;
        mainGbc.insets = new Insets(0, 0, 0, 0); 
        frame.add(loginPanel, mainGbc);
    }

    // GETTERS: Allow the controller to access components
    public String getLoginEmailString() {
        return emailField.getText();
    }

    public String getLoginEmailPassword() {
        return new String(passwordField.getPassword());
    }

    public void addSubmitListener(java.awt.event.ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void show() {
        frame.setVisible(true);
    }
}