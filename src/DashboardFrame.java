import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DashboardFrame extends JFrame {

    private JButton btnNewOrder;
    private JButton btnViewOrders;
    private JButton btnDelivery;
    private JButton btnSettings;
    private JButton btnExit;

    private CateringPriceManager priceManager;

    public DashboardFrame() {
        this.priceManager = new CateringPriceManager();

        setTitle("Restaurant Management System - Almayda");
        setSize(500, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel title = new JLabel("RESTAURANT MANAGEMENT SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ===== BUTTON PANEL =====
        JPanel panel = new JPanel(new GridLayout(5, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        btnNewOrder = new JButton("New Order");
        btnViewOrders = new JButton("View Orders");
        btnDelivery = new JButton("Delivery Management");
        btnSettings = new JButton("Settings");
        btnExit = new JButton("Exit");

        panel.add(btnNewOrder);
        panel.add(btnViewOrders);
        panel.add(btnDelivery);
        panel.add(btnSettings);
        panel.add(btnExit);

        add(panel, BorderLayout.CENTER);

        // ===== NAVIGATION LOGIC =====
        btnNewOrder.addActionListener(e -> openSubScreen(new NewOrderFrame(priceManager)));
        btnViewOrders.addActionListener(e -> openSubScreen(new OrderListFrame()));
        btnDelivery.addActionListener(e -> openSubScreen(new DeliveryFrame()));
        btnSettings.addActionListener(e -> new SettingsDialog(this, priceManager, "MENU").setVisible(true));
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void openSubScreen(JFrame subFrame) {
        this.setVisible(false);

        subFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                DashboardFrame.this.setVisible(true);
            }
        });

        subFrame.setVisible(true);
    }
}