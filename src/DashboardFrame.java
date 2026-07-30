import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DashboardFrame extends JFrame {

    private JButton btnNewOrder;
    private JButton btnViewOrders;
    private JButton btnDelivery;
    private JButton btnDrivers;
    private JToggleButton btnThemeToggle;
    private JButton btnExit;

    public DashboardFrame() {
        setTitle("Restaurant Management System - Almayda");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel title = new JLabel("RESTAURANT MANAGEMENT SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ===== BUTTON PANEL =====
        JPanel panel = new JPanel(new GridLayout(6, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        btnNewOrder = new JButton("New Order");
        btnViewOrders = new JButton("View Orders");
        btnDelivery = new JButton("Delivery Management");
        btnDrivers = new JButton("Drivers");
        btnThemeToggle = new JToggleButton("🌙 Dark Mode");
        btnExit = new JButton("Exit");

        panel.add(btnNewOrder);
        panel.add(btnViewOrders);
        panel.add(btnDelivery);
        panel.add(btnDrivers);
        panel.add(btnThemeToggle);
        panel.add(btnExit);

        add(panel, BorderLayout.CENTER);

        // ===== NAVIGATION LOGIC (ONE WINDOW AT A TIME) =====

        btnNewOrder.addActionListener(e -> openSubScreen(new NewOrderFrame()));

        btnViewOrders.addActionListener(e -> openSubScreen(new OrderListFrame()));

        btnDelivery.addActionListener(e -> openSubScreen(new DeliveryFrame()));

        btnDrivers.addActionListener(e -> openSubScreen(new DriverFrame()));

        // Theme Toggle
        btnThemeToggle.addActionListener(e -> {
            if (btnThemeToggle.isSelected()) {
                btnThemeToggle.setText("☀️ Light Mode");
                Main.switchTheme(true);
            } else {
                btnThemeToggle.setText("🌙 Dark Mode");
                Main.switchTheme(false);
            }
        });

        // Exit Application
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    /**
     * Hides the Dashboard and opens the sub-frame.
     * Restores the Dashboard automatically when the sub-frame is closed.
     */
    private void openSubScreen(JFrame subFrame) {
        // 1. Hide the Dashboard
        this.setVisible(false);

        // 2. Listen for when the sub-frame closes (or when Back is clicked)
        subFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // 3. Show the Dashboard again
                DashboardFrame.this.setVisible(true);
            }
        });

        // 4. Show the target page
        subFrame.setVisible(true);
    }
}