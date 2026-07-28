import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JButton btnNewOrder;
    private JButton btnViewOrders;
    private JButton btnDelivery;
    private JButton btnDrivers;
    private JButton btnExit;


    public DashboardFrame() {

        setTitle("Restaurant Management System");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        // ===== TITLE =====

        JLabel title = new JLabel(
                "RESTAURANT MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        add(title, BorderLayout.NORTH);




        // ===== BUTTON PANEL =====

        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(5,1,20,20)
        );


        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        50,100,50,100
                )
        );



        btnNewOrder =
                new JButton("New Order");


        btnViewOrders =
                new JButton("View Orders");



        btnDelivery =
                new JButton("Delivery Management");



        btnDrivers =
                new JButton("Drivers");



        btnExit =
                new JButton("Exit");




        panel.add(btnNewOrder);

        panel.add(btnViewOrders);

        panel.add(btnDelivery);

        panel.add(btnDrivers);

        panel.add(btnExit);




        add(
                panel,
                BorderLayout.CENTER
        );






        // ===== BUTTON ACTIONS =====




        btnNewOrder.addActionListener(e -> {

            new NewOrderFrame();

        });






        btnViewOrders.addActionListener(e -> {

            new OrderListFrame();

        });






        btnDelivery.addActionListener(e -> {

            new DeliveryFrame();

        });






        btnDrivers.addActionListener(e -> {

            new DriverFrame();

        });






        btnExit.addActionListener(e -> {

            System.exit(0);

        });





        setVisible(true);

    }

}