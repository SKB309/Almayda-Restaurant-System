import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class OrderListFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnUndo;
    private JButton btnRefresh;
    private JButton btnBack;
    private JButton btnWhatsApp;

    private JTextField txtFilter;

    public OrderListFrame() {
        setTitle("Order List - Almayda");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ==========================
        // SEARCH FILTER PANEL
        // ==========================
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.add(new JLabel("Search Orders:"));

        txtFilter = new JTextField(30);
        filterPanel.add(txtFilter);
        add(filterPanel, BorderLayout.NORTH);

        txtFilter.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterOrders(); }
            public void removeUpdate(DocumentEvent e) { filterOrders(); }
            public void changedUpdate(DocumentEvent e) { filterOrders(); }
        });

        // ==========================
        // TABLE SETUP
        // ==========================
        String[] columns = {
                "Bill No", "Customer", "Phone", "Location",
                "Order", "Date", "Time", "Price",
                "Advance", "Due", "Payment Method", "Payment Status"
        };

        // Table model with disabled cell editing
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);

        // Double-click row shortcut to edit order
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    openEditFrameForSelectedRow();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ==========================
        // BUTTON PANEL
        // ==========================
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnEdit = new JButton("Edit Order");
        btnDelete = new JButton("Delete Order");
        btnUndo = new JButton("Undo Delete");
        btnRefresh = new JButton("Refresh");
        btnWhatsApp = new JButton("Send WhatsApp");
        btnBack = new JButton("Back");

        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnUndo);
        panel.add(btnRefresh);
        panel.add(btnWhatsApp);
        panel.add(btnBack);

        add(panel, BorderLayout.SOUTH);

        // Load records
        loadOrders();

        // ==========================
        // BUTTON EVENTS
        // ==========================
        btnRefresh.addActionListener(e -> {
            txtFilter.setText("");
            loadOrders();
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this order?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                int billNo = Integer.parseInt(table.getValueAt(row, 0).toString());
                OrderManager.deleteOrder(billNo);
                loadOrders();
                JOptionPane.showMessageDialog(this, "Order deleted successfully.");
            }
        });

        btnUndo.addActionListener(e -> {
            if (OrderManager.undoDelete()) {
                loadOrders();
                JOptionPane.showMessageDialog(this, "Deleted order restored successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Nothing to restore.");
            }
        });

        btnEdit.addActionListener(e -> openEditFrameForSelectedRow());

        btnWhatsApp.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int billNo = Integer.parseInt(table.getValueAt(row, 0).toString());
            Order order = OrderManager.findOrder(billNo);

            if (order != null) {
                WhatsAppSender.send(order);
            } else {
                JOptionPane.showMessageDialog(this, "Order details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void openEditFrameForSelectedRow() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int billNo = Integer.parseInt(table.getValueAt(row, 0).toString());
        Order order = OrderManager.findOrder(billNo);

        if (order != null) {
            EditOrderFrame editFrame = new EditOrderFrame(billNo); // ✅ Passing int billNo
            editFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    loadOrders(); // Auto-refresh table when Edit window closes
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Order not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================
    // LOAD ORDERS
    // ==========================
    public void loadOrders() {
        model.setRowCount(0);
        for (Order order : OrderManager.getOrders()) {
            addOrderToTable(order);
        }
    }

    // ==========================
    // ADD ROW TO TABLE
    // ==========================
    private void addOrderToTable(Order order) {
        Customer customer = order.getCustomer();
        model.addRow(new Object[]{
                order.getBillNumber(),
                customer.getCustomerName(), // Updated from getName()
                customer.getPhoneNumber(),  // Updated from getPhone()
                customer.getLocation(),
                order.getOrderDetails(),
                order.getOrderDate(),       // Updated from getDate()
                order.getOrderTime(),       // Updated from getTime()
                String.format(Locale.US, "%.3f", order.getTotalPrice()),
                String.format(Locale.US, "%.3f", order.getAdvancePayment()),
                String.format(Locale.US, "%.3f", order.getDueAmount()),
                order.getPaymentMethod(),
                order.getPaymentStatus()
        });
    }

    // ==========================
    // FILTER ALL COLUMNS
    // ==========================
    private void filterOrders() {
        model.setRowCount(0);
        String search = txtFilter.getText().toLowerCase().trim();

        for (Order order : OrderManager.getOrders()) {
            Customer customer = order.getCustomer();

            String allData = (order.getBillNumber() + " " +
                    customer.getCustomerName() + " " +
                    customer.getPhoneNumber() + " " +
                    customer.getLocation() + " " +
                    order.getOrderDetails() + " " +
                    order.getOrderDate() + " " +
                    order.getOrderTime() + " " +
                    order.getTotalPrice() + " " +
                    order.getAdvancePayment() + " " +
                    order.getDueAmount() + " " +
                    order.getPaymentMethod() + " " +
                    order.getPaymentStatus()).toLowerCase();

            if (allData.contains(search)) {
                addOrderToTable(order);
            }
        }
    }
}