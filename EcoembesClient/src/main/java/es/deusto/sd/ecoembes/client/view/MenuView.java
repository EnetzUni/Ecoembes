package es.deusto.sd.ecoembes.client.view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import es.deusto.sd.ecoembes.client.util.GuiUtils;
import java.awt.*;
import java.net.URL;

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

    // Sidebar Buttons
    private JButton homeButton;
    private JButton recyclingPlantsButton;
    private JButton assignmentsButton;
    private JButton dumpstersButton;
    private JButton assignDumpstersButton;
    private JButton logoutButton;

    private JButton homeLogoutButton;

    // View Identifiers
    public static final String VIEW_HOME = "HOME";
    public static final String VIEW_RECYCLING = "RECYCLING";
    public static final String VIEW_ASSIGNMENTS = "ASSIGNMENTS";
    public static final String VIEW_DUMPSTERS = "DUMPSTERS";
    public static final String VIEW_ASSIGN_DUMPSTERS = "ASSIGN_DUMPSTERS";

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

        // Initialize Panels
        homePanel = createHomePanel();
        
        // Titles will now be Green (#95c767)
        recyclingPanel = createTitlePanel("Recycling Plants:");
        assignmentsPanel = createTitlePanel("Assignments:");
        dumpstersPanel = createTitlePanel("Dumpsters:");
        assignDumpstersPanel = createTitlePanel("Assign Dumpsters:");

        // Add Panels
        cardPanel.add(homePanel, VIEW_HOME);
        cardPanel.add(recyclingPanel, VIEW_RECYCLING);
        cardPanel.add(assignmentsPanel, VIEW_ASSIGNMENTS);
        cardPanel.add(dumpstersPanel, VIEW_DUMPSTERS);
        cardPanel.add(assignDumpstersPanel, VIEW_ASSIGN_DUMPSTERS);

        mainContent.add(cardPanel, BorderLayout.CENTER);

        frame.add(sideMenu, BorderLayout.WEST);
        frame.add(mainContent, BorderLayout.CENTER);
    }

    // --- Dynamic Content Updaters ---

    public void updateRecyclingTable(JTable table) {
        updatePanelContent(recyclingPanel, table);
    }

    public void updateAssignmentsTable(JTable table) {
        updatePanelContent(assignmentsPanel, table);
    }

    public void updateDumpstersTable(JTable table) {
        updatePanelContent(dumpstersPanel, table);
    }

    private void updatePanelContent(JPanel panel, JTable table) {
        // 1. Remove old table (Center Component)
        BorderLayout layout = (BorderLayout) panel.getLayout();
        Component center = layout.getLayoutComponent(BorderLayout.CENTER);
        if (center != null) {
            panel.remove(center);
        }

        // 2. Apply Custom Styles (Green Header, White Bg)
        setupTableStyle(table);

        // 3. Wrap in ScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // 4. Add Insets (Padding: Top=0, Left=10, Bottom=10, Right=10)
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        // 5. Add to View
        panel.add(wrapperPanel, BorderLayout.CENTER);
        
        // Refresh
        panel.revalidate();
        panel.repaint();
    }

    private void setupTableStyle(JTable table) {
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(ECO_GREEN); // Green Header
        header.setForeground(Color.WHITE); 
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setOpaque(true);
        
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    // --- Initialization Methods ---
    private void initSideMenu() {
        sideMenu = new JPanel();
        sideMenu.setBackground(ECO_GREEN);
        sideMenu.setPreferredSize(new Dimension(MENU_WIDTH, 600));
        sideMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        homeButton = new JButton("Home");
        recyclingPlantsButton = new JButton("Recycling Plants");
        assignmentsButton = new JButton("Assignments");
        dumpstersButton = new JButton("Dumpsters");
        assignDumpstersButton = new JButton("Assign Dumpsters");
        logoutButton = new JButton("Logout");

        styleAndAddButton(homeButton);
        styleAndAddButton(recyclingPlantsButton);
        styleAndAddButton(assignmentsButton);
        styleAndAddButton(dumpstersButton);
        styleAndAddButton(assignDumpstersButton);
        styleAndAddButton(logoutButton);
    }

    private void styleAndAddButton(JButton btn) {
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setFocusPainted(false);
        sideMenu.add(btn);
    }

    private void initLogoArea() {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 35, 0));

        JLabel topLogoLabel = new JLabel();
        URL bannerURL = getClass().getResource("/EcoembesLogo4320x1080.png");
        ImageIcon bannerIcon = (bannerURL != null) 
            ? new ImageIcon(bannerURL) 
            : new ImageIcon("EcoembesClient/src/resources/images/EcoembesLogo4320x1080.png");

        if (bannerIcon.getIconWidth() > 0) {
            Image scaledImage = bannerIcon.getImage().getScaledInstance(500, 125, Image.SCALE_SMOOTH);
            topLogoLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            topLogoLabel.setText("Logo Not Found");
        }
        logoPanel.add(topLogoLabel);
        mainContent.add(logoPanel, BorderLayout.NORTH);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel thankYouLabel = new JLabel("Thank you for using Ecoembes");
        thankYouLabel.setForeground(ECO_GREEN);
        thankYouLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0); 
        panel.add(thankYouLabel, gbc);

        homeLogoutButton = new JButton("Logout");
        homeLogoutButton.setPreferredSize(new Dimension(150, 40));
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(homeLogoutButton, gbc);

        return panel;
    }

    private JPanel createTitlePanel(String titleText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(ECO_GREEN); // This sets the Title Color
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); 

        panel.add(title, BorderLayout.NORTH);
        return panel;
    }

    // --- Public Getters & Control ---
    public void showCard(String cardName) { cardLayout.show(cardPanel, cardName); }
    public JFrame getFrame() { return frame; }
    public void show() { frame.setVisible(true); }

    public JButton getHomeButton() { return homeButton; }
    public JButton getRecyclingPlantsButton() { return recyclingPlantsButton; }
    public JButton getAssignmentsButton() { return assignmentsButton; }
    public JButton getDumpstersButton() { return dumpstersButton; }
    public JButton getAssignDumpstersButton() { return assignDumpstersButton; }
    public JButton getLogoutButton() { return logoutButton; }
    public JButton getHomeLogoutButton() { return homeLogoutButton; }
}