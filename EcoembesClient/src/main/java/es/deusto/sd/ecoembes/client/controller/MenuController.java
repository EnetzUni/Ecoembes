package es.deusto.sd.ecoembes.client.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.List;

import es.deusto.sd.ecoembes.client.view.MenuView;
import es.deusto.sd.ecoembes.client.view.LoginView;
import es.deusto.sd.ecoembes.client.util.GuiUtils;

import es.deusto.sd.ecoembes.client.model.Dumpster;
import es.deusto.sd.ecoembes.client.model.RecyclingPlant;
import es.deusto.sd.ecoembes.client.model.Assignment;
import es.deusto.sd.ecoembes.client.proxies.IEcoembesServiceProxy;
import es.deusto.sd.ecoembes.client.proxies.HttpServiceProxy;

public class MenuController {

    private final MenuView view;
    private final IEcoembesServiceProxy serviceProxy;
    private String token; 
    
    public MenuController(MenuView view, String token) {
        this.view = view;
        this.token = token;
        this.serviceProxy = new HttpServiceProxy();
        initController();
    }

    private void initController() {
        // Navigation
        view.getHomeButton().addActionListener(e -> view.showCard(MenuView.VIEW_HOME));

        view.getRecyclingPlantsButton().addActionListener(e -> {
            loadRecyclingPlants();
            view.showCard(MenuView.VIEW_RECYCLING);
        });
        
        view.getAssignmentsButton().addActionListener(e -> {
            loadAssignments();
            view.showCard(MenuView.VIEW_ASSIGNMENTS);
        });

        view.getDumpstersButton().addActionListener(e -> {
            loadDumpsters();
            view.showCard(MenuView.VIEW_DUMPSTERS);
        });

        view.getAssignDumpstersButton().addActionListener(e -> {
            view.showCard(MenuView.VIEW_ASSIGN_DUMPSTERS);
        });

        // Logout
        ActionListener logoutAction = e -> performLogout();
        view.getLogoutButton().addActionListener(logoutAction);
        view.getHomeLogoutButton().addActionListener(logoutAction);
    }

    private void loadRecyclingPlants() {
        try {
            List<RecyclingPlant> plants = serviceProxy.getPlants(token);
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Location", "Capacity"}, 0);
            
            for (RecyclingPlant p : plants) {
                model.addRow(new Object[]{p.id(), p.name(), p.location(), p.capacity()});
            }
            
            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            
            // Pass raw table to View for styling and display
            view.updateRecyclingTable(table);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error loading plants: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadDumpsters() {
        try {
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
            
            view.updateDumpstersTable(table);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error loading dumpsters: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadAssignments() {
        try {
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
            
            view.updateAssignmentsTable(table);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error loading assignments: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void performLogout() {
        if (this.token != null) {
            try {
                serviceProxy.logout(token);
            } catch (Exception e) {
                System.err.println("Logout error: " + e.getMessage());
            }
        }
        this.token = null;
        LoginView loginView = new LoginView();
        new LoginController(loginView); 
        GuiUtils.switchFrames(view.getFrame(), loginView.getFrame());
    }
}