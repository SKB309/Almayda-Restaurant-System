import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrderListFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnUndo;
    private JButton btnRefresh;
    private JButton btnBack;
    private JButton btnWhatsApp;


    public OrderListFrame() {

        setTitle("Order List");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ==========================
        // TABLE
        // ==========================

        String[] columns = {
                "Bill No",
                "Customer",
                "Phone",
                "Location",
                "Order",
                "Date",
                "Time",
                "Price",
                "Advance",
                "Due"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);


        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ==========================
        // BUTTON PANEL
        // ==========================

        JPanel panel = new JPanel();

        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnUndo = new JButton("Undo Delete");
        btnRefresh = new JButton("Refresh");
        btnBack = new JButton("Back");
        btnWhatsApp = new JButton("WhatsApp");

        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnUndo);
        panel.add(btnRefresh);
        panel.add(btnBack);
        panel.add(btnWhatsApp);

        add(panel, BorderLayout.SOUTH);

        // Load data
        loadOrders();

        // ==========================
        // BUTTON EVENTS
        // ==========================

        btnRefresh.addActionListener(e -> loadOrders());

        btnDelete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select an order.");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this order?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {

                int billNo = Integer.parseInt(
                        table.getValueAt(row, 0).toString());

                OrderManager.deleteOrder(billNo);

                loadOrders();

                JOptionPane.showMessageDialog(this,
                        "Order deleted successfully.");

            }

        });

        btnUndo.addActionListener(e -> {

            if (OrderManager.undoDelete()) {

                loadOrders();

                JOptionPane.showMessageDialog(
                        this,
                        "Deleted order restored."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Nothing to restore."
                );

            }

        });

        btnEdit.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select an order."
                );

                return;

            }

            int billNo = Integer.parseInt(
                    table.getValueAt(row, 0).toString());

            new EditOrderFrame(billNo);

        });

        btnBack.addActionListener(e -> dispose());

        setVisible(true);

        btnWhatsApp.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Select an order first."
                );

                return;
            }

            int billNo = Integer.parseInt(
                    table.getValueAt(row, 0).toString());

            Order order = OrderManager.findOrder(billNo);

            if (order != null) {

                WhatsAppSender.send(order);

            }

        });
    }





    // ==========================
    // LOAD ORDERS
    // ==========================

    private void loadOrders() {

        model.setRowCount(0);

        for (Order order : OrderManager.getOrders()) {

            Customer customer = order.getCustomer();

            model.addRow(new Object[]{
                    order.getBillNumber(),
                    customer.getCustomerName(),
                    customer.getPhoneNumber(),
                    customer.getLocation(),
                    order.getOrderDetails(),
                    order.getOrderDate(),
                    order.getOrderTime(),
                    order.getTotalPrice(),
                    order.getAdvancePayment(),
                    order.getDueAmount()
            });

        }

    }

}