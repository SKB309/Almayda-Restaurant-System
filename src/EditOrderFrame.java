import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditOrderFrame extends JFrame {

    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtLocation;

    private JTextArea txtOrder;

    private JTextField txtDate;
    private JTextField txtTime;

    private JTextField txtPrice;
    private JTextField txtAdvance;
    private JTextField txtDue;

    private JComboBox<String> cmbOrderType;
    private JComboBox<String> cmbPaymentMethod;
    private JComboBox<String> cmbPaymentStatus;

    private JButton btnUpdate;
    private JButton btnCalculate;
    private JButton btnCancel;

    private int billNumber;

    private Border defaultBorder;
    private final Border errorBorder = BorderFactory.createLineBorder(new Color(220, 53, 69), 2);

    public EditOrderFrame(int billNumber) {
        this.billNumber = billNumber;

        setTitle("Edit Order #" + billNumber + " - Almayda");
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("EDIT ORDER #" + billNumber, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        txtName = new JTextField();
        txtPhone = new JTextField();
        txtLocation = new JTextField();

        // Limit txtPhone input to max 8 digits in real-time
        ((AbstractDocument) txtPhone.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if ((fb.getDocument().getLength() + string.length()) <= 8 && string.matches("\\d+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                int newLength = fb.getDocument().getLength() - length + text.length();
                if (newLength <= 8 && text.matches("\\d+")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        txtOrder = new JTextArea(3, 20);
        txtDate = new JTextField();
        txtTime = new JTextField();

        txtPrice = new JTextField();
        txtAdvance = new JTextField();

        txtDue = new JTextField();
        txtDue.setEditable(false);
        txtDue.setFocusable(false);

        defaultBorder = txtName.getBorder();

        // Combo Boxes
        cmbOrderType = new JComboBox<>(new String[]{"Pickup", "Delivery"});
        cmbPaymentMethod = new JComboBox<>(new String[]{"None", "Cash", "Card", "Online", "Cash & Card", "Multiple"});
        cmbPaymentStatus = new JComboBox<>(new String[]{"Pending", "Partially Paid", "Paid"});

        // Adding components to form
        form.add(new JLabel("Customer Name"));
        form.add(txtName);

        form.add(new JLabel("Phone Number (8 digits, starts with 9 or 7)"));
        form.add(txtPhone);

        form.add(new JLabel("Location"));
        form.add(txtLocation);

        form.add(new JLabel("Order Details"));
        form.add(new JScrollPane(txtOrder));

        form.add(new JLabel("Date"));
        form.add(txtDate);

        form.add(new JLabel("Time"));
        form.add(txtTime);

        form.add(new JLabel("Total Price (OMR)"));
        form.add(txtPrice);

        form.add(new JLabel("Advance (OMR)"));
        form.add(txtAdvance);

        form.add(new JLabel("Due (OMR)"));
        form.add(txtDue);

        form.add(new JLabel("Order Type"));
        form.add(cmbOrderType);

        form.add(new JLabel("Payment Method"));
        form.add(cmbPaymentMethod);

        form.add(new JLabel("Payment Status"));
        form.add(cmbPaymentStatus);

        add(form, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel();
        btnCalculate = new JButton("Calculate Due");
        btnUpdate = new JButton("Update");
        btnCancel = new JButton("Cancel");

        buttons.add(btnCalculate);
        buttons.add(btnUpdate);
        buttons.add(btnCancel);

        add(buttons, BorderLayout.SOUTH);

        // Automatic Calculation Listeners
        DocumentListener autoCalcListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculateDue(); }
            public void removeUpdate(DocumentEvent e) { calculateDue(); }
            public void changedUpdate(DocumentEvent e) { calculateDue(); }
        };
        txtPrice.getDocument().addDocumentListener(autoCalcListener);
        txtAdvance.getDocument().addDocumentListener(autoCalcListener);

        // Load existing order data into fields
        loadOrder();

        // Button Actions
        btnCalculate.addActionListener(e -> calculateDue());
        btnUpdate.addActionListener(e -> updateOrder());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void resetFieldBorders() {
        txtName.setBorder(defaultBorder);
        txtPhone.setBorder(defaultBorder);
        txtLocation.setBorder(defaultBorder);
        txtDate.setBorder(defaultBorder);
        txtTime.setBorder(defaultBorder);
        txtPrice.setBorder(defaultBorder);
        txtAdvance.setBorder(defaultBorder);
    }

    // =========================
    // LOAD ORDER
    // =========================
    private void loadOrder() {
        Order order = OrderManager.findOrder(billNumber);

        if (order == null) {
            JOptionPane.showMessageDialog(this, "Order not found");
            dispose();
            return;
        }

        Customer customer = order.getCustomer();

        txtName.setText(customer.getCustomerName());
        txtPhone.setText(customer.getPhoneNumber());
        txtLocation.setText(customer.getLocation());

        txtOrder.setText(order.getOrderDetails());
        txtDate.setText(order.getOrderDate());
        txtTime.setText(order.getOrderTime());

        txtPrice.setText(String.valueOf(order.getTotalPrice()));
        txtAdvance.setText(String.valueOf(order.getAdvancePayment()));
        txtDue.setText(String.valueOf(order.getDueAmount()));

        // 🟢 SAFE SELECTION (Fixes "Delivery" not appearing)
        setSelectedCaseInsensitive(cmbOrderType, order.getOrderType());
        setSelectedCaseInsensitive(cmbPaymentMethod, order.getPaymentMethod());
        setSelectedCaseInsensitive(cmbPaymentStatus, order.getPaymentStatus());
    }

    // =========================
    // CALCULATE DUE
    // =========================
    private void calculateDue() {
        try {
            String priceText = txtPrice.getText().trim();
            String advanceText = txtAdvance.getText().trim();

            double price = priceText.isEmpty() ? 0.0 : Double.parseDouble(priceText);
            double advance = advanceText.isEmpty() ? 0.0 : Double.parseDouble(advanceText);

            double due = Math.max(0, price - advance);
            txtDue.setText(String.format(Locale.US, "%.3f", due));

            // Automatic Payment Status logic
            if (price > 0) {
                if (advance >= price) {
                    cmbPaymentStatus.setSelectedItem("Paid");
                } else if (advance > 0) {
                    cmbPaymentStatus.setSelectedItem("Partially Paid");
                } else {
                    cmbPaymentStatus.setSelectedItem("Pending");
                }
            } else {
                cmbPaymentStatus.setSelectedItem("Pending");
            }

        } catch (NumberFormatException e) {
            txtDue.setText("Invalid input");
        }
    }

    // =========================
    // UPDATE ORDER
    // =========================
    private void updateOrder() {
        resetFieldBorders();

        List<String> errors = new ArrayList<>();
        JComponent firstErrorComponent = null;

        // 1. Customer Name Validation
        String name = txtName.getText().trim();
        if (name.isEmpty() || name.matches("^\\d+$")) {
            errors.add("Customer Name is missing or invalid (cannot be numbers only)");
            txtName.setBorder(errorBorder);
            if (firstErrorComponent == null) firstErrorComponent = txtName;
        }

        // 2. Phone Validation (Exactly 8 digits starting with 9 or 7)
        String phone = txtPhone.getText().trim();
        if (phone.length() != 8 || !phone.matches("^[97]\\d{7}$")) {
            errors.add("Phone Number must be exactly 8 digits and start with 9 or 7");
            txtPhone.setBorder(errorBorder);
            if (firstErrorComponent == null) firstErrorComponent = txtPhone;
        }

        // 3. Location Validation (Required if Delivery)
        boolean isDelivery = "Delivery".equalsIgnoreCase(cmbOrderType.getSelectedItem().toString());
        if (isDelivery && txtLocation.getText().trim().isEmpty()) {
            errors.add("Location is required for Delivery orders");
            txtLocation.setBorder(errorBorder);
            if (firstErrorComponent == null) firstErrorComponent = txtLocation;
        }

        // 4. Order Details Validation
        if (txtOrder.getText().trim().isEmpty()) {
            errors.add("Order Details cannot be empty");
            if (firstErrorComponent == null) firstErrorComponent = txtOrder;
        }

        // 5. Total Price & Advance Validation
        double priceVal = 0.0;
        try {
            priceVal = Double.parseDouble(txtPrice.getText().trim());
            if (priceVal <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            errors.add("Total Price must be a valid number greater than 0");
            txtPrice.setBorder(errorBorder);
            if (firstErrorComponent == null) firstErrorComponent = txtPrice;
        }

        double advanceVal = 0.0;
        try {
            String advStr = txtAdvance.getText().trim();
            if (!advStr.isEmpty()) {
                advanceVal = Double.parseDouble(advStr);
                if (advanceVal < 0 || (priceVal > 0 && advanceVal > priceVal)) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException e) {
            errors.add("Advance Payment cannot be negative or exceed Total Price");
            txtAdvance.setBorder(errorBorder);
            if (firstErrorComponent == null) firstErrorComponent = txtAdvance;
        }

        // Display Validation Errors if any
        if (!errors.isEmpty()) {
            StringBuilder msg = new StringBuilder("Please fix the following errors:\n\n");
            for (String err : errors) {
                msg.append("• ").append(err).append("\n");
            }
            JOptionPane.showMessageDialog(this, msg.toString(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            if (firstErrorComponent != null) {
                firstErrorComponent.requestFocus();
            }
            return;
        }

        // Perform Order Update
        try {
            Customer customer = new Customer(
                    name,
                    phone,
                    txtLocation.getText().trim()
            );

            Order updatedOrder = new Order(
                    customer,
                    billNumber,
                    txtOrder.getText().trim(),
                    txtDate.getText().trim(),
                    txtTime.getText().trim(),
                    priceVal,
                    advanceVal,
                    cmbPaymentMethod.getSelectedItem().toString(),
                    cmbPaymentStatus.getSelectedItem().toString(),
                    cmbOrderType.getSelectedItem().toString()
            );

            OrderManager.updateOrder(updatedOrder);

            JOptionPane.showMessageDialog(this, "Order Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setSelectedCaseInsensitive(JComboBox<String> comboBox, String valueToSelect) {
        if (valueToSelect == null) return;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equalsIgnoreCase(valueToSelect.trim())) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }
}