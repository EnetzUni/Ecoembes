package es.deusto.sd.ecoembes.client.view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import es.deusto.sd.ecoembes.client.util.GuiUtils;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class MenuView {

    private JFrame frame;
    private JPanel sideMenu;
    private JPanel mainContent; 
    
    // CardLayout components
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Content Panels
    private JPanel homePanel;
    private JPanel recyclingPanel;
    private JPanel assignmentsPanel;
    private JPanel dumpstersPanel;
    private JPanel assignDumpstersPanel;
    private JPanel createDumpstersPanel;

    // --- "Assign Dumpsters" Components ---
    private JPanel assignDumpstersLeftContainer; 
    private JButton btnToDumpsters;
    private JButton btnToAssignments;
    private JButton btnToPlants;
    private JLabel assignLeftTitleLabel;
    
    private JComboBox<String> plantComboBox; 
    private JButton dumpsterDropdownBtn; 
    private JButton createAssignmentButton;

    // --- "Create Dumpsters" Components ---
    private JPanel createDumpstersLeftContainer; 
    private JLabel createLeftTitleLabel;
    
    // Form Fields
    private JTextField createLocationField;
    private JTextField createCapacityField;
    private JSpinner createContainerSpinner;
    private JSlider createFillSlider;
    private JLabel createFillValueLabel;
    private JButton createDumpstersSubmitButton; 

    // Sidebar Buttons
    private JButton homeButton;
    private JButton recyclingPlantsButton;
    private JButton assignmentsButton;
    private JButton dumpstersButton;
    private JButton assignDumpstersButton;
    private JButton createDumpstersButton;
    private JButton logoutButton;
    private JButton homeLogoutButton;

    // View Identifiers
    public static final String VIEW_HOME = "HOME";
    public static final String VIEW_RECYCLING = "RECYCLING";
    public static final String VIEW_ASSIGNMENTS = "ASSIGNMENTS";
    public static final String VIEW_DUMPSTERS = "DUMPSTERS";
    public static final String VIEW_ASSIGN_DUMPSTERS = "ASSIGN_DUMPSTERS";
    public static final String VIEW_CREATE_DUMPSTERS = "CREATE_DUMPSTERS";

    public static final String STATE_DUMPSTERS = "DUMPSTERS";
    public static final String STATE_ASSIGNMENTS = "ASSIGNMENTS";
    public static final String STATE_PLANTS = "PLANTS";

    private final int MENU_WIDTH = 250;
    private final Color ECO_GREEN = Color.decode("#95c767");

    public MenuView() {
        this.frame = GuiUtils.setupFrame();
        frame.setLayout(new BorderLayout());
        initSideMenu();
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        initLogoArea();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);

        homePanel = createHomePanel();
        recyclingPanel = createTitlePanel("Recycling Plants:");
        assignmentsPanel = createTitlePanel("Assignments:");
        dumpstersPanel = createTitlePanel("Dumpsters:");
        assignDumpstersPanel = createAssignDumpstersPanel();
        createDumpstersPanel = createCreateDumpstersPanel();

        cardPanel.add(homePanel, VIEW_HOME);
        cardPanel.add(recyclingPanel, VIEW_RECYCLING);
        cardPanel.add(assignmentsPanel, VIEW_ASSIGNMENTS);
        cardPanel.add(dumpstersPanel, VIEW_DUMPSTERS);
        cardPanel.add(assignDumpstersPanel, VIEW_ASSIGN_DUMPSTERS);
        cardPanel.add(createDumpstersPanel, VIEW_CREATE_DUMPSTERS);

        mainContent.add(cardPanel, BorderLayout.CENTER);
        frame.add(sideMenu, BorderLayout.WEST);
        frame.add(mainContent, BorderLayout.CENTER);
    }

    // [MODIFIED] Spaced out components + Read-only Spinner
    private JPanel createCreateDumpstersPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Text
        JLabel mainTitle = new JLabel("Create Dumpsters:");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainTitle.setForeground(ECO_GREEN);
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(mainTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // --- LEFT PANEL (Table) ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        
        createLeftTitleLabel = new JLabel("Current Dumpsters:");
        createLeftTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        createLeftTitleLabel.setForeground(ECO_GREEN);
        createLeftTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        createLeftTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        leftPanel.add(createLeftTitleLabel, BorderLayout.NORTH);

        createDumpstersLeftContainer = new JPanel(new BorderLayout());
        createDumpstersLeftContainer.setBackground(Color.WHITE);
        leftPanel.add(createDumpstersLeftContainer, BorderLayout.CENTER);

        // --- RIGHT PANEL (Form) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel rightTitle = new JLabel("Create Dumpsters:");
        rightTitle.setFont(new Font("Arial", Font.BOLD, 14));
        rightTitle.setForeground(ECO_GREEN);
        rightTitle.setHorizontalAlignment(SwingConstants.CENTER);
        rightTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        rightPanel.add(rightTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        // KEY CHANGE: Set weighty to 1.0 for all components to distribute them evenly
        gbc.weighty = 1.0; 
        gbc.anchor = GridBagConstraints.CENTER; 

        // 1. Location Input
        JPanel locPanel = new JPanel(new BorderLayout(0, 5));
        locPanel.setBackground(Color.WHITE);
        locPanel.add(new JLabel("Location:"), BorderLayout.NORTH);
        createLocationField = new JTextField();
        createLocationField.setPreferredSize(new Dimension(0, 35));
        locPanel.add(createLocationField, BorderLayout.CENTER);
        formPanel.add(locPanel, gbc);

        // 2. Max Capacity Input
        gbc.gridy++;
        JPanel capPanel = new JPanel(new BorderLayout(0, 5));
        capPanel.setBackground(Color.WHITE);
        capPanel.add(new JLabel("Max Capacity:"), BorderLayout.NORTH);
        createCapacityField = new JTextField();
        createCapacityField.setPreferredSize(new Dimension(0, 35));
        capPanel.add(createCapacityField, BorderLayout.CENTER);
        formPanel.add(capPanel, gbc);

        // 3. Container Count (Spinner)
        gbc.gridy++;
        JPanel spinPanel = new JPanel(new BorderLayout(0, 5));
        spinPanel.setBackground(Color.WHITE);
        spinPanel.add(new JLabel("Container Count:"), BorderLayout.NORTH);
        
        createContainerSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        createContainerSpinner.setPreferredSize(new Dimension(0, 35));
        
        // Disable typing in the text field, arrows only
        JComponent editor = createContainerSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor) editor).getTextField().setBackground(Color.WHITE);
        }
        spinPanel.add(createContainerSpinner, BorderLayout.CENTER);
        formPanel.add(spinPanel, gbc);

        // 4. Fill Level (Slider)
        gbc.gridy++;
        JPanel sliderContainer = new JPanel(new BorderLayout(0, 5));
        sliderContainer.setBackground(Color.WHITE);
        
        JPanel sliderHeader = new JPanel(new BorderLayout());
        sliderHeader.setBackground(Color.WHITE);
        JLabel lblFill = new JLabel("Fill Level:");
        createFillValueLabel = new JLabel("0%");
        createFillValueLabel.setFont(new Font("Arial", Font.BOLD, 12));
        createFillValueLabel.setForeground(ECO_GREEN);
        sliderHeader.add(lblFill, BorderLayout.WEST);
        sliderHeader.add(createFillValueLabel, BorderLayout.EAST);
        
        createFillSlider = new JSlider(0, 100, 0);
        createFillSlider.setBackground(Color.WHITE);
        createFillSlider.setForeground(ECO_GREEN); 
        createFillSlider.addChangeListener(e -> createFillValueLabel.setText(createFillSlider.getValue() + "%"));
        
        sliderContainer.add(sliderHeader, BorderLayout.NORTH);
        sliderContainer.add(createFillSlider, BorderLayout.CENTER);
        formPanel.add(sliderContainer, gbc);

        // 5. Submit Button
        gbc.gridy++;
        // Keep button at the bottom, but share vertical space
        createDumpstersSubmitButton = new JButton("Create Dumpster");
        createDumpstersSubmitButton.setBackground(ECO_GREEN);
        createDumpstersSubmitButton.setForeground(Color.WHITE);
        createDumpstersSubmitButton.setFont(new Font("Arial", Font.BOLD, 14));
        createDumpstersSubmitButton.setFocusPainted(false);
        createDumpstersSubmitButton.setPreferredSize(new Dimension(0, 45));
        
        // Wrap button so it doesn't stretch vertically too much, but sits centered in its cell
        JPanel btnWrapper = new JPanel(new BorderLayout());
        btnWrapper.setBackground(Color.WHITE);
        btnWrapper.add(createDumpstersSubmitButton, BorderLayout.CENTER);
        // Add a bit of top margin to separate slightly more if needed
        btnWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        formPanel.add(btnWrapper, gbc);

        rightPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createAssignDumpstersPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel mainTitle = new JLabel("Assign Dumpsters:");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainTitle.setForeground(ECO_GREEN);
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(mainTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // LEFT PANEL 
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        assignLeftTitleLabel = new JLabel("Current Dumpsters:");
        assignLeftTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        assignLeftTitleLabel.setForeground(ECO_GREEN);
        assignLeftTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        assignLeftTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        leftPanel.add(assignLeftTitleLabel, BorderLayout.NORTH);

        assignDumpstersLeftContainer = new JPanel(new BorderLayout());
        assignDumpstersLeftContainer.setBackground(Color.WHITE);
        leftPanel.add(assignDumpstersLeftContainer, BorderLayout.CENTER);

        JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnContainer.setBackground(Color.WHITE);
        btnToDumpsters = new JButton("Switch to Dumpsters");
        btnToAssignments = new JButton("Switch to Assignments");
        btnToPlants = new JButton("Switch to Recycling Plants");
        Dimension btnDim = new Dimension(220, 35);
        btnToDumpsters.setPreferredSize(btnDim);
        btnToAssignments.setPreferredSize(btnDim);
        btnToPlants.setPreferredSize(btnDim);
        btnContainer.add(btnToDumpsters);
        btnContainer.add(btnToAssignments);
        btnContainer.add(btnToPlants);
        leftPanel.add(btnContainer, BorderLayout.SOUTH);

        // RIGHT PANEL
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel rightTitle = new JLabel("Create Assignment:");
        rightTitle.setFont(new Font("Arial", Font.BOLD, 14));
        rightTitle.setForeground(ECO_GREEN);
        rightTitle.setHorizontalAlignment(SwingConstants.CENTER);
        rightTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        rightPanel.add(rightTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblPlant = new JLabel("Select Recycling Plant:");
        gbc.anchor = GridBagConstraints.SOUTHWEST; 
        formPanel.add(lblPlant, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        plantComboBox = new JComboBox<>();
        plantComboBox.setBackground(Color.WHITE);
        plantComboBox.setPreferredSize(new Dimension(0, 30));
        formPanel.add(plantComboBox, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        JLabel lblDump = new JLabel("Select Dumpsters:");
        formPanel.add(lblDump, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        dumpsterDropdownBtn = new JButton("Select Dumpsters"); 
        dumpsterDropdownBtn.setBackground(Color.WHITE);
        dumpsterDropdownBtn.setHorizontalAlignment(SwingConstants.CENTER);
        dumpsterDropdownBtn.setPreferredSize(new Dimension(0, 30));
        formPanel.add(dumpsterDropdownBtn, gbc);

        gbc.gridy++;
        gbc.weighty = 1.5; 
        gbc.anchor = GridBagConstraints.CENTER;
        createAssignmentButton = new JButton("Create Assignment");
        createAssignmentButton.setBackground(ECO_GREEN);
        createAssignmentButton.setForeground(Color.WHITE);
        createAssignmentButton.setFont(new Font("Arial", Font.BOLD, 14));
        createAssignmentButton.setFocusPainted(false);
        createAssignmentButton.setPreferredSize(new Dimension(0, 40));
        formPanel.add(createAssignmentButton, gbc);

        rightPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    // --- Helper Methods ---

    public void populatePlantDropdown(List<String> items) {
        plantComboBox.removeAllItems();
        for (String item : items) plantComboBox.addItem(item);
    }

    public String getSelectedPlantString() { 
        return (String) plantComboBox.getSelectedItem(); 
    }
    
    // Switch Buttons logic (Only for Assign view now)
    public void updateLeftSwitchButtons(String activeState) {
        btnToDumpsters.setVisible(!activeState.equals(STATE_DUMPSTERS));
        btnToAssignments.setVisible(!activeState.equals(STATE_ASSIGNMENTS));
        btnToPlants.setVisible(!activeState.equals(STATE_PLANTS));
        if (activeState.equals(STATE_DUMPSTERS)) assignLeftTitleLabel.setText("Current Dumpsters:");
        else if (activeState.equals(STATE_ASSIGNMENTS)) assignLeftTitleLabel.setText("Current Assignments:");
        else if (activeState.equals(STATE_PLANTS)) assignLeftTitleLabel.setText("Current Recycling Plants:");
    }

    public void updateAssignDumpstersLeftTable(JTable table) { updateContainer(assignDumpstersLeftContainer, table); }
    public void updateCreateDumpstersLeftTable(JTable table) { updateContainer(createDumpstersLeftContainer, table); }

    public void updateRecyclingTable(JTable table) { updatePanelContent(recyclingPanel, table); }
    public void updateAssignmentsTable(JTable table) { updatePanelContent(assignmentsPanel, table); }
    public void updateDumpstersTable(JTable table) { updatePanelContent(dumpstersPanel, table); }

    private void updatePanelContent(JPanel panel, JTable table) {
        BorderLayout layout = (BorderLayout) panel.getLayout();
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);
        if (center != null) panel.remove(center);
        updateContainer(panel, table);
    }
    
    private void updateContainer(JPanel container, JTable table) {
        setupTableStyle(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        
        if (container == assignDumpstersLeftContainer || container == createDumpstersLeftContainer) {
            container.removeAll();
            container.add(scrollPane, BorderLayout.CENTER);
        } else {
            wrapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            wrapper.add(scrollPane, BorderLayout.CENTER);
            container.add(wrapper, BorderLayout.CENTER);
        }
        container.revalidate(); 
        container.repaint();
    }

    private void setupTableStyle(JTable table) {
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        JTableHeader header = table.getTableHeader();
        header.setBackground(ECO_GREEN);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setOpaque(true);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    // --- Init Side Menu ---
    private void initSideMenu() {
        sideMenu = new JPanel(); sideMenu.setBackground(ECO_GREEN); sideMenu.setPreferredSize(new Dimension(MENU_WIDTH, 650));
        sideMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        
        homeButton = new JButton("Home"); 
        recyclingPlantsButton = new JButton("Recycling Plants");
        assignmentsButton = new JButton("Assignments"); 
        dumpstersButton = new JButton("Dumpsters");
        assignDumpstersButton = new JButton("Assign Dumpsters"); 
        createDumpstersButton = new JButton("Create Dumpsters"); 
        logoutButton = new JButton("Logout");
        
        styleAndAddButton(homeButton); 
        styleAndAddButton(recyclingPlantsButton); 
        styleAndAddButton(assignmentsButton);
        styleAndAddButton(dumpstersButton); 
        styleAndAddButton(assignDumpstersButton);
        styleAndAddButton(createDumpstersButton);
        styleAndAddButton(logoutButton);
    }
    private void styleAndAddButton(JButton btn) { btn.setPreferredSize(new Dimension(200, 40)); btn.setFocusPainted(false); sideMenu.add(btn); }
    
    private void initLogoArea() {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); logoPanel.setBackground(Color.WHITE); logoPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 35, 0));
        JLabel topLogoLabel = new JLabel();
        URL bannerURL = getClass().getResource("/EcoembesLogo4320x1080.png");
        ImageIcon bannerIcon = (bannerURL != null) ? new ImageIcon(bannerURL) : new ImageIcon("EcoembesClient/src/resources/images/EcoembesLogo4320x1080.png");
        if (bannerIcon.getIconWidth() > 0) topLogoLabel.setIcon(new ImageIcon(bannerIcon.getImage().getScaledInstance(500, 125, Image.SCALE_SMOOTH)));
        else topLogoLabel.setText("Logo Not Found");
        logoPanel.add(topLogoLabel); mainContent.add(logoPanel, BorderLayout.NORTH);
    }
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(); panel.setBackground(Color.WHITE); panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel thankYouLabel = new JLabel("Thank you for using Ecoembes");
        thankYouLabel.setForeground(ECO_GREEN); thankYouLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 20, 0); panel.add(thankYouLabel, gbc);
        homeLogoutButton = new JButton("Logout"); homeLogoutButton.setPreferredSize(new Dimension(150, 40));
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 0, 0); panel.add(homeLogoutButton, gbc);
        return panel;
    }
    private JPanel createTitlePanel(String titleText) {
        JPanel panel = new JPanel(new BorderLayout()); panel.setBackground(Color.WHITE);
        JLabel title = new JLabel(titleText); title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(ECO_GREEN); title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); panel.add(title, BorderLayout.NORTH);
        return panel;
    }

    // --- Getters ---
    public void showCard(String cardName) { cardLayout.show(cardPanel, cardName); }
    public JFrame getFrame() { return frame; }
    public void show() { frame.setVisible(true); }
    
    public JButton getHomeButton() { return homeButton; }
    public JButton getRecyclingPlantsButton() { return recyclingPlantsButton; }
    public JButton getAssignmentsButton() { return assignmentsButton; }
    public JButton getDumpstersButton() { return dumpstersButton; }
    public JButton getAssignDumpstersButton() { return assignDumpstersButton; }
    public JButton getCreateDumpstersButton() { return createDumpstersButton; }
    public JButton getLogoutButton() { return logoutButton; }
    public JButton getHomeLogoutButton() { return homeLogoutButton; }
    
    // Assign View
    public JButton getBtnToDumpsters() { return btnToDumpsters; }
    public JButton getBtnToAssignments() { return btnToAssignments; }
    public JButton getBtnToPlants() { return btnToPlants; }
    public JButton getCreateAssignmentButton() { return createAssignmentButton; }
    public JButton getSelectDumpstersButton() { return dumpsterDropdownBtn; }

    // Create View
    public JButton getCreateDumpstersSubmitButton() { return createDumpstersSubmitButton; }
    public JTextField getCreateLocationField() { return createLocationField; }
    public JTextField getCreateCapacityField() { return createCapacityField; }
    public JSpinner getCreateContainerSpinner() { return createContainerSpinner; }
    public JSlider getCreateFillSlider() { return createFillSlider; }
}