package es.deusto.sd.ecoembes.client.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    
    private List<Long> selectedDumpsterIds = new ArrayList<>();
    
    @SuppressWarnings("unused")
    private String assignLeftState = MenuView.STATE_DUMPSTERS;

    public MenuController(MenuView view, String token) {
        this.view = view;
        this.token = token;
        this.serviceProxy = new HttpServiceProxy();
        initController();
    }

    private void initController() {
        // --- Navigation ---
        view.getHomeButton().addActionListener(e -> view.showCard(MenuView.VIEW_HOME));
        view.getRecyclingPlantsButton().addActionListener(e -> {
            loadRecyclingPlants();
            view.showCard(MenuView.VIEW_RECYCLING);
        });
        view.getAssignmentsButton().addActionListener(e -> {
            loadAssignmentsForMainView();
            view.showCard(MenuView.VIEW_ASSIGNMENTS);
        });
        view.getDumpstersButton().addActionListener(e -> {
            loadDumpstersForMainView();
            view.showCard(MenuView.VIEW_DUMPSTERS);
        });

        // --- Assign Dumpsters View Entry ---
        view.getAssignDumpstersButton().addActionListener(e -> {
            setAssignState(MenuView.STATE_DUMPSTERS);
            loadAssignmentFormData(); 
            view.showCard(MenuView.VIEW_ASSIGN_DUMPSTERS);
        });

        // --- Create Dumpsters View Entry ---
        view.getCreateDumpstersButton().addActionListener(e -> {
            loadDumpstersForCreateView(); 
            view.showCard(MenuView.VIEW_CREATE_DUMPSTERS);
        });

        // --- Assign View Switch Button Logic ---
        view.getBtnToDumpsters().addActionListener(e -> setAssignState(MenuView.STATE_DUMPSTERS));
        view.getBtnToAssignments().addActionListener(e -> setAssignState(MenuView.STATE_ASSIGNMENTS));
        view.getBtnToPlants().addActionListener(e -> setAssignState(MenuView.STATE_PLANTS));

        // --- Form Logic ---
        view.getCreateAssignmentButton().addActionListener(e -> performCreateAssignment());
        
        view.getCreateDumpstersSubmitButton().addActionListener(e -> {
    try {
        // 1. RECOGER DATOS (Usando los componentes correctos de MenuView)
        String location = view.getCreateLocationField().getText();
        String capacityText = view.getCreateCapacityField().getText();
        
        // JSpinner devuelve un Object, hay que castear a Integer
        int containerCount = (Integer) view.getCreateContainerSpinner().getValue();
        
        // JSlider devuelve un int (0 a 100), representa el % de llenado inicial
        int fillPercentage = view.getCreateFillSlider().getValue();

        // VALIDACIONES BÁSICAS
        if (location == null || location.trim().isEmpty() || capacityText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view.getFrame(), "❌ Por favor, introduce Ubicación y Capacidad.");
            return;
        }

        // 2. CONVERSIONES Y CÁLCULOS
        float maxCapacity = Float.parseFloat(capacityText);
        
        // CUIDADO AQUÍ: Tu record valida que fillLevel <= maxCapacity.
        // El slider da un %, así que calculamos los litros/kilos reales.
        float currentFillLevel = maxCapacity * (fillPercentage / 100.0f);

        // 3. CREAR OBJETO DUMPSTER
        // ID va a 0L porque el servidor lo autogenera.
        Dumpster newDumpster = new Dumpster(0L, location, maxCapacity, containerCount, currentFillLevel);

        System.out.println("📤 Enviando a crear: " + newDumpster);

        // 4. LLAMADA AL SERVIDOR
        Dumpster created = serviceProxy.createDumpster(newDumpster, token);

        // 5. ACTUALIZAR INTERFAZ
        if (created != null) {
            JOptionPane.showMessageDialog(view.getFrame(), 
                "✅ Contenedor creado con éxito.\nID asignado: " + created.id());

            // Limpiar formulario
            view.getCreateLocationField().setText("");
            view.getCreateCapacityField().setText("");
            view.getCreateContainerSpinner().setValue(1); // Reset al valor por defecto
            view.getCreateFillSlider().setValue(0);       // Reset slider a 0

            
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), 
                "⚠️ El servidor no devolvió el contenedor creado (Posible error de validación).");
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(view.getFrame(), 
            "❌ Error de formato: La capacidad debe ser un número (ej. 1000.5).");
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view.getFrame(), 
            "❌ Error de conexión: " + ex.getMessage());
    }
});
        
        view.getSelectDumpstersButton().addActionListener(e -> {
            try {
                List<Dumpster> dumpsters = serviceProxy.getDumpsters(token);
                showDumpsterSelectionDialog(dumpsters, selectedDumpsterIds, view.getSelectDumpstersButton());
            } catch (Exception ex) {
                handleError("Error loading dumpsters", ex);
            }
        });

        // --- Logout ---
        ActionListener logoutAction = e -> performLogout();
        view.getLogoutButton().addActionListener(logoutAction);
        view.getHomeLogoutButton().addActionListener(logoutAction);
    }

    private void setAssignState(String newState) {
        this.assignLeftState = newState;
        view.updateLeftSwitchButtons(newState);
        if (MenuView.STATE_DUMPSTERS.equals(newState)) loadDumpstersForAssignView();
        else if (MenuView.STATE_ASSIGNMENTS.equals(newState)) loadAssignmentsForAssignView();
        else if (MenuView.STATE_PLANTS.equals(newState)) loadPlantsForAssignView();
    }

    // --- Form Handling ---
    private void loadAssignmentFormData() {
        try {
            List<RecyclingPlant> plants = serviceProxy.getPlants(token);
            List<String> plantItems = new ArrayList<>();
            for (RecyclingPlant p : plants) if (p != null) plantItems.add(p.id() + " - " + p.name());
            view.populatePlantDropdown(plantItems);
            selectedDumpsterIds.clear();
            updateDumpsterButtonText(selectedDumpsterIds, view.getSelectDumpstersButton());
        } catch (Exception e) {
            handleError("Error loading form data", e);
        }
    }

    private void showDumpsterSelectionDialog(List<Dumpster> dumpsters, List<Long> targetList, JButton targetButton) {
        JDialog dialog = new JDialog(view.getFrame(), "Select Dumpsters", true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        List<JCheckBox> checkBoxes = new ArrayList<>();

        for (Dumpster d : dumpsters) {
            if (d == null) continue;
            String label = "Dumpster " + d.id() + " (" + d.location() + ")";
            JCheckBox cb = new JCheckBox(label);
            if (targetList.contains(d.id())) cb.setSelected(true);
            cb.putClientProperty("id", d.id());
            checkBoxes.add(cb);
            checkBoxPanel.add(cb);
        }

        JScrollPane scrollPane = new JScrollPane(checkBoxPanel);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        dialog.add(scrollPane);

        JButton btnOk = new JButton("Confirm Selection");
        btnOk.addActionListener(event -> {
            targetList.clear();
            for (JCheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    Object idObj = cb.getClientProperty("id");
                    if (idObj instanceof Long) targetList.add((Long) idObj);
                }
            }
            updateDumpsterButtonText(targetList, targetButton);
            dialog.dispose();
        });

        dialog.add(btnOk);
        dialog.pack();
        dialog.setLocationRelativeTo(view.getFrame());
        dialog.setVisible(true);
    }

    private void updateDumpsterButtonText(List<Long> list, JButton btn) {
        if (list.isEmpty()) {
            btn.setText("Select Dumpster");
        } else {
            String ids = list.stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.joining(", "));
            if (ids.length() > 20) btn.setText(list.size() + " Dumpsters Selected");
            else btn.setText("Dumpsters: " + ids);
        }
    }

    private void performCreateAssignment() {
        try {
            String plantString = view.getSelectedPlantString();
            if (plantString == null || plantString.trim().isEmpty()) {
                JOptionPane.showMessageDialog(view.getFrame(), "Please select a Recycling Plant.");
                return;
            }
            long plantId = Long.parseLong(plantString.split(" - ")[0].trim());
            List<Long> cleanDumpsterIds = selectedDumpsterIds.stream().filter(Objects::nonNull).collect(Collectors.toList());

            if (cleanDumpsterIds.isEmpty()) {
                JOptionPane.showMessageDialog(view.getFrame(), "Please select at least one Dumpster.");
                return;
            }

            serviceProxy.createAssignment(plantId, cleanDumpsterIds, token);
            JOptionPane.showMessageDialog(view.getFrame(), "Assignment created successfully!");
            
            selectedDumpsterIds.clear();
            updateDumpsterButtonText(selectedDumpsterIds, view.getSelectDumpstersButton());
            setAssignState(MenuView.STATE_ASSIGNMENTS);
            view.showCard(MenuView.VIEW_ASSIGNMENTS);
            loadAssignmentsForMainView(); 

        } catch (Exception e) {
            handleError("Error creating assignment", e);
        }
    }

    // --- Data Loaders ---
    private void loadRecyclingPlants() {
        try { view.updateRecyclingTable(createRecyclingTable(serviceProxy.getPlants(token))); } 
        catch (Exception ex) { handleError("Error loading plants", ex); }
    }
    private void loadPlantsForAssignView() {
        try { view.updateAssignDumpstersLeftTable(createRecyclingTable(serviceProxy.getPlants(token))); } 
        catch (Exception ex) { handleError("Error loading plants", ex); }
    }

    private void loadDumpstersForMainView() {
        try { view.updateDumpstersTable(createDumpsterTable(serviceProxy.getDumpsters(token))); } 
        catch (Exception ex) { handleError("Error loading dumpsters", ex); }
    }
    private void loadDumpstersForAssignView() {
        try { view.updateAssignDumpstersLeftTable(createDumpsterTable(serviceProxy.getDumpsters(token))); } 
        catch (Exception ex) { handleError("Error loading dumpsters", ex); }
    }
    private void loadDumpstersForCreateView() {
        try { view.updateCreateDumpstersLeftTable(createDumpsterTable(serviceProxy.getDumpsters(token))); } 
        catch (Exception ex) { handleError("Error loading dumpsters", ex); }
    }

    private void loadAssignmentsForMainView() {
        try { view.updateAssignmentsTable(createAssignmentTable(serviceProxy.getAssignments(token))); } 
        catch (Exception ex) { handleError("Error loading assignments", ex); }
    }
    private void loadAssignmentsForAssignView() {
        try { view.updateAssignDumpstersLeftTable(createAssignmentTable(serviceProxy.getAssignments(token))); } 
        catch (Exception ex) { handleError("Error loading assignments", ex); }
    }

    // --- Table Helpers ---
    private JTable createRecyclingTable(List<RecyclingPlant> plants) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Location", "Capacity"}, 0);
        for (RecyclingPlant p : plants) model.addRow(new Object[]{p.id(), p.name(), p.location(), p.capacity()});
        return createConfiguredTable(model);
    }
    private JTable createDumpsterTable(List<Dumpster> dumpsters) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Location", "Max Capacity", "Containers", "Fill Level"}, 0);
        for (Dumpster d : dumpsters) model.addRow(new Object[]{d.id(), d.location(), d.maxCapacity(), d.containerCount(), String.format("%.2f %%", d.fillLevel() * 100)});
        return createConfiguredTable(model);
    }
    private JTable createAssignmentTable(List<Assignment> assignments) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Date", "EmployeeID", "PlantID", "Dumpsters"}, 0);
        for (Assignment a : assignments) {
            List<Long> dumpsterIds = a.dumpsters().stream().map(Dumpster::id).toList();
            model.addRow(new Object[]{a.id(), a.date(), a.employeeId(), a.recyclingPlantId(), dumpsterIds});
        }
        return createConfiguredTable(model);
    }
    private JTable createConfiguredTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }
    private void handleError(String msg, Exception ex) {
        String errorText = ex.getMessage();
        if (errorText != null && errorText.contains("400")) {
            JOptionPane.showMessageDialog(view.getFrame(), "Validation Error: The server rejected the data.\nCheck that all IDs are valid.", "Server Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), msg + ": " + errorText);
        }
        ex.printStackTrace();
    }
    private void performLogout() {
        if (this.token != null) {
            try { serviceProxy.logout(token); } 
            catch (Exception e) { System.err.println("Logout error: " + e.getMessage()); }
        }
        this.token = null;
        LoginView loginView = new LoginView();
        new LoginController(loginView); 
        GuiUtils.switchFrames(view.getFrame(), loginView.getFrame());
    }
}