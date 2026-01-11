package es.deusto.sd.ecoembes.client.view;

import javax.swing.*;
import java.awt.*;

public class MenuView {
    private JFrame frame;
    private JButton btnViewDumpsters;
    private JButton btnViewPlants;
    private JButton btnAssignDumpsters;
    private JButton btnViewAssignments;
    private JButton btnLogout;

    public MenuView() {
        frame = new JFrame("Ecoembes - Employee Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        btnViewDumpsters = new JButton("View Dumpsters");
        btnViewPlants = new JButton("View Recycling Plants");
        btnAssignDumpsters = new JButton("Assign Dumpsters");
        btnViewAssignments = new JButton("View Assignments");
        btnLogout = new JButton("Logout");

        JButton[] buttons = {btnViewDumpsters, btnViewPlants, btnAssignDumpsters, btnViewAssignments, btnLogout};
        Color primaryColor = Color.decode("#95c767");
        for (JButton b : buttons) {
            b.setPreferredSize(new Dimension(250, 50));
            b.setBackground(primaryColor);
            b.setForeground(Color.WHITE);
        }

        gbc.gridx = 0; gbc.gridy = 0; frame.add(btnViewDumpsters, gbc);
        gbc.gridy++; frame.add(btnViewPlants, gbc);
        gbc.gridy++; frame.add(btnAssignDumpsters, gbc);
        gbc.gridy++; frame.add(btnViewAssignments, gbc);
        gbc.gridy++; frame.add(btnLogout, gbc);
    }

    // Getters
    public JButton getBtnViewDumpsters() { return btnViewDumpsters; }
    public JButton getBtnViewPlants() { return btnViewPlants; }
    public JButton getBtnAssignDumpsters() { return btnAssignDumpsters; }
    public JButton getBtnViewAssignments() { return btnViewAssignments; }
    public JButton getBtnLogout() { return btnLogout; }
    public JFrame getFrame() { return frame; }
    public void show() { frame.setVisible(true); }
}
