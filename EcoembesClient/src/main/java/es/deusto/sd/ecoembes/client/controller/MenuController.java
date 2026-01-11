package es.deusto.sd.ecoembes.client.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import es.deusto.sd.ecoembes.client.model.Dumpster;
import es.deusto.sd.ecoembes.client.model.RecyclingPlant;
import es.deusto.sd.ecoembes.client.model.Assignment;
import es.deusto.sd.ecoembes.client.proxies.IEcoembesServiceProxy;
import es.deusto.sd.ecoembes.client.proxies.HttpServiceProxy;
import es.deusto.sd.ecoembes.client.util.GuiUtils;
import es.deusto.sd.ecoembes.client.view.LoginView;
import es.deusto.sd.ecoembes.client.view.MenuView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MenuController implements ActionListener {

    private final MenuView view;
    private final IEcoembesServiceProxy serviceProxy;
    private final String token;


    public MenuController(MenuView view, String token) {
        this.view = view;
        this.token = token;
        this.serviceProxy = new HttpServiceProxy();

        view.getBtnViewDumpsters().addActionListener(this);
        view.getBtnViewPlants().addActionListener(this);
        view.getBtnAssignDumpsters().addActionListener(this);
        view.getBtnViewAssignments().addActionListener(this);
        view.getBtnLogout().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == view.getBtnViewDumpsters()) {
                showDumpsters();
            } else if (e.getSource() == view.getBtnViewPlants()) {
                showPlants();
            } else if (e.getSource() == view.getBtnAssignDumpsters()) {
                assignDumpsters();
            } else if (e.getSource() == view.getBtnViewAssignments()) {
                showAssignments();
            } else if (e.getSource() == view.getBtnLogout()) {
                serviceProxy.logout(token);
                LoginView loginView = new LoginView();
                GuiUtils.switchFrames(view.getFrame(), loginView.getFrame());
                loginView.show();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showDumpsters() throws Exception {
        List<Dumpster> dumpsters = serviceProxy.getDumpsters(token);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Location", "Max Capacity", "Containers", "Fill Level"}, 0
        );
        for (Dumpster d : dumpsters) {
            model.addRow(new Object[]{
                d.id(),
                d.location(),
                d.maxCapacity(),
                d.containerCount(),
                String.format("%.2f %%", d.fillLevel() * 100)
            });
        }
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        JOptionPane.showMessageDialog(view.getFrame(), scrollPane, "Dumpsters", JOptionPane.PLAIN_MESSAGE);
    }

    private void showPlants() throws Exception {
        List<RecyclingPlant> plants = serviceProxy.getPlants(token);
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Location", "Capacity"}, 0);
        for (RecyclingPlant p : plants) {
            model.addRow(new Object[]{p.id(), p.name(), p.location(), p.capacity()});
        }
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        JOptionPane.showMessageDialog(view.getFrame(), scrollPane, "Recycling Plants", JOptionPane.PLAIN_MESSAGE);
    }

    private void assignDumpsters() throws Exception {
        List<Dumpster> dumpsters = serviceProxy.getDumpsters(token);
        List<RecyclingPlant> plants = serviceProxy.getPlants(token);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Select","ID","Location","Max Capacity"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) { return columnIndex == 0 ? Boolean.class : Object.class; }
        };

        for (Dumpster d : dumpsters) {
            model.addRow(new Object[]{false, d.id(), d.location(), d.maxCapacity()});
        }

        JTable table = new JTable(model);
        JComboBox<RecyclingPlant> comboBox = new JComboBox<>();
        for (RecyclingPlant p : plants) comboBox.addItem(p);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(comboBox, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(view.getFrame(), panel, "Assign Dumpsters", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            List<Long> selectedIds = new ArrayList<>();
            for (int i = 0; i < table.getRowCount(); i++) {
                if (Boolean.TRUE.equals(table.getValueAt(i, 0))) 
                    selectedIds.add((Long)table.getValueAt(i, 1));
            }

            if (selectedIds.isEmpty()) {
                JOptionPane.showMessageDialog(view.getFrame(), "No dumpsters selected");
                return;
            }

            RecyclingPlant selectedPlant = (RecyclingPlant) comboBox.getSelectedItem();
            if (selectedPlant == null) {
                JOptionPane.showMessageDialog(view.getFrame(), "No plant selected");
                return;
            }

            System.out.println("Assigning dumpsters " + selectedIds + " to plant " + selectedPlant.id());


            serviceProxy.createAssignment(selectedPlant.id(), selectedIds, token);

            JOptionPane.showMessageDialog(view.getFrame(), "Assignments successful!");
        }
    }

    private void showAssignments() throws Exception {
        List<Assignment> assignments = serviceProxy.getAssignments(token);
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Date", "EmployeeID", "PlantID", "Dumpsters"}, 0
        );
        for (Assignment a : assignments) {
            List<Long> dumpsterIds = a.dumpsters().stream().map(Dumpster::id).toList();
            model.addRow(new Object[]{a.id(), a.date(), a.employeeId(), a.recyclingPlantId(), dumpsterIds});
        }
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        JOptionPane.showMessageDialog(view.getFrame(), scrollPane, "Assignments", JOptionPane.PLAIN_MESSAGE);
    }
}
