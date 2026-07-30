import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.toedter.calendar.JDateChooser;

public class NewOrderFrame extends JFrame {

    private JTextField txtName, txtPhone, txtLocation;
    private JTextField txtOrder, txtTime;
    private JTextField txtPrice, txtAdvance, txtDue;
    private JDateChooser dateChooser;
    private JComboBox<String> cmbOrderType, cmbPaymentMethod, cmbPaymentStatus;
    private JButton btnSave, btnCalculate, btnClear, btnBack;

    private Border defaultBorder;
    private final Border errorBorder = BorderFactory.createLineBorder(new Color(220, 53, 69), 2);

    public NewOrderFrame() {
        setTitle("New Order - Almayda");
        setSize(550, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Customer Info
        panel.add(new JLabel("Customer Name:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Phone Number (8 digits starting with 9 or 7):"));
        txtPhone = new JTextField();
        panel.add(txtPhone);

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

        panel.add(new JLabel("Location:"));
        txtLocation = new JTextField();
        panel.add(txtLocation);

        defaultBorder = txtName.getBorder();

        // Order Details
        panel.add(new JLabel("Order Details:"));
        txtOrder = new JTextField();
        panel.add(txtOrder);

        panel.add(new JLabel("Order Type:"));
        cmbOrderType = new JComboBox<>(new String[]{"Pickup", "Delivery"});
        panel.add(cmbOrderType);

        // Date & Time
        panel.add(new JLabel("Order Date:"));
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        dateChooser.setDateFormatString("EEEE, dd-MM-yyyy");
        panel.add(dateChooser);

        panel.add(new JLabel("Time:"));
        txtTime = new JTextField();
        panel.add(txtTime);

        // Payment Info
        panel.add(new JLabel("Total Price (OMR):"));
        txtPrice = new JTextField();
        panel.add(txtPrice);

        panel.add(new JLabel("Advance (OMR):"));
        txtAdvance = new JTextField();
        panel.add(txtAdvance);

        panel.add(new JLabel("Due Amount (OMR):"));
        txtDue = new JTextField();
        txtDue.setEditable(false);
        txtDue.setFocusable(false);
        panel.add(txtDue);

        panel.add(new JLabel("Payment Method:"));
        cmbPaymentMethod = new JComboBox<>(new String[]{"None", "Cash", "Card", "Online"});
        panel.add(cmbPaymentMethod);

        panel.add(new JLabel("Payment Status:"));
        cmbPaymentStatus = new JComboBox<>(new String[]{"Pending", "Partially Paid", "Paid"});
        panel.add(cmbPaymentStatus);

        // Buttons
        btnCalculate = new JButton("Recalculate");
        btnSave = new JButton("Save Order");
        btnClear = new JButton("Clear");
        btnBack = new JButton("Back");

        panel.add(btnCalculate);
        panel.add(btnSave);
        panel.add(btnClear);
        panel.add(btnBack);

        add(panel);

        // Automatic Calculation Listeners
        DocumentListener autoCalcListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculateDue(); }
            public void removeUpdate(DocumentEvent e) { calculateDue(); }
            public void changedUpdate(DocumentEvent e) { calculateDue(); }
        };
        txtPrice.getDocument().addDocumentListener(autoCalcListener);
        txtAdvance.getDocument().addDocumentListener(autoCalcListener);

        // Actions
        btnCalculate.addActionListener(e -> calculateDue());
        btnSave.addActionListener(e -> saveOrder());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void resetFieldBorders() {
        txtName.setBorder(defaultBorder);
        txtPhone.setBorder(defaultBorder);
        txtLocation.setBorder(defaultBorder);
        txtOrder.setBorder(defaultBorder);
        txtTime.setBorder(defaultBorder);
        txtPrice.setBorder(defaultBorder);
        txtAdvance.setBorder(defaultBorder);
    }

    private void calculateDue() {
        try {
            String priceText = txtPrice.getText().trim();
            String advanceText = txtAdvance.getText().trim();

            double price = priceText.isEmpty() ? 0.0 : Double.parseDouble(priceText);
            double advance = advanceText.isEmpty() ? 0.0 : Double.parseDouble(advanceText);

            double due = Math.max(0, price - advance);
            txtDue.setText(String.format(Locale.US, "%.3f", due));

            // Automatic Payment Status logic based on Price and Advance
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

    private void saveOrder() {
        resetFieldBorders();

        List<String> errors = new ArrayList<>();
        JTextField firstErrorField = null;

        // 1. Customer Name Validation
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            errors.add("اسم العميل مفقود / Customer Name is missing");
            txtName.setBorder(errorBorder);
            if (firstErrorField == null) firstErrorField = txtName;
        } else if (name.matches("^\\d+$")) {
            errors.add("اسم العميل لا يمكن أن يكون أرقاماً فقط / Customer Name cannot be digits only");
            txtName.setBorder(errorBorder);
            if (firstErrorField == null) firstErrorField = txtName;
        }

        // 2. Phone Validation (Must be exactly 8 digits AND start with 9 or 7)
        String phone = txtPhone.getText().trim();
        if (phone.isEmpty()) {
            errors.add("رقم الهاتف مفقود / Phone Number is missing");
            txtPhone.setBorder(errorBorder);
            if (firstErrorField == null) firstErrorField = txtPhone;
        } else if (phone.length() != 8 || !phone.matches("^[97]\\d{7}$")) {
            errors.add("رقم الهاتف يجب أن يتكون من 8 أرقام بالضبط ويبدأ بـ 9 أو 7 / Phone Number must be exactly 8 digits and start with 9 or 7");
            txtPhone.setBorder(errorBorder);
            if (firstErrorField == null) firstErrorField = txtPhone;
        }

        // 3. Location Validation (Required if order type is Delivery)
        boolean isDelivery = "Delivery".equalsIgnoreCase(cmbOrderType.getSelectedItem().toString());
        if (isDelivery && txtLocation.getText().trim().isEmpty()) {
            errors.add("الموقع مطلوب لطلبات التوصيل / Location required for Delivery");
            txtLocation.setBorder(errorBorder);
            if (firstErrorField == null) firstErrorField = txtLocation;
        }

        // 4. Order Details Validation
        if (txtOrder.getText().trim().isEmpty()) {
            errors.add("تفاصيل الطلب مفقودة / Order Details are missing");
            txtOrder.setBorder(errorBorder);
            if (firstErrorField == null) firstErrorField = txtOrder;
        }

        // 5. Time Validation
        if (txtTime.getText().trim().isEmpty()) {
            errors.add("الوقت مفقود / Time is missing");
            txtTime.setBorder(errorBorder);
            if (firstErrorField == null) firstErrorField = txtTime;
        }

        // 6. Total Price Validation
        String priceStr = txtPrice.getText().trim();
        double priceVal = 0.0;
        if (priceStr.isEmpty()) {
            errors.add("السعر الإجمالي مفقود / Total Price is missing");
            txtPrice.setBorder(errorBorder);
            if (firstErrorField == null) firstErrorField = txtPrice;
        } else {
            try {
                priceVal = Double.parseDouble(priceStr);
                if (priceVal <= 0) {
                    errors.add("السعر الإجمالي يجب أن يكون أكبر من 0 / Total Price must be greater than 0");
                    txtPrice.setBorder(errorBorder);
                    if (firstErrorField == null) firstErrorField = txtPrice;
                }
            } catch (NumberFormatException e) {
                errors.add("السعر الإجمالي يجب أن يكون رقماً صحيحاً أو عشرياً / Total Price must be a valid number");
                txtPrice.setBorder(errorBorder);
                if (firstErrorField == null) firstErrorField = txtPrice;
            }
        }

        // 7. Advance Payment Validation
        String advanceStr = txtAdvance.getText().trim();
        double advanceVal = 0.0;
        if (!advanceStr.isEmpty()) {
            try {
                advanceVal = Double.parseDouble(advanceStr);
                if (advanceVal < 0) {
                    errors.add("الدفعة المقدمة لا يمكن أن تكون بالسالب / Advance cannot be negative");
                    txtAdvance.setBorder(errorBorder);
                    if (firstErrorField == null) firstErrorField = txtAdvance;
                } else if (priceVal > 0 && advanceVal > priceVal) {
                    errors.add("الدفعة المقدمة لا يمكن أن تكون أكبر من الإجمالي / Advance cannot exceed Total Price");
                    txtAdvance.setBorder(errorBorder);
                    if (firstErrorField == null) firstErrorField = txtAdvance;
                }
            } catch (NumberFormatException e) {
                errors.add("الدفعة المقدمة يجب أن تكون رقماً / Advance must be a valid number");
                txtAdvance.setBorder(errorBorder);
                if (firstErrorField == null) firstErrorField = txtAdvance;
            }
        }

        // If there are validation errors, display popup and highlight fields
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder("يرجى تصحيح الأخطاء التالية:\n\n");
            for (String err : errors) {
                message.append("• ").append(err).append("\n");
            }

            JOptionPane.showMessageDialog(
                    this,
                    message.toString(),
                    "خطأ في إدخال البيانات",
                    JOptionPane.WARNING_MESSAGE
            );

            if (firstErrorField != null) {
                firstErrorField.requestFocus();
            }
            return;
        }

        // Save Order Execution
        try {
            Date selectedDate = dateChooser.getDate() != null ? dateChooser.getDate() : new Date();
            String date = new SimpleDateFormat("EEEE, dd-MM-yyyy", new Locale("ar")).format(selectedDate);

            Customer customer = new Customer(
                    name,
                    phone,
                    txtLocation.getText().trim()
            );

            String orderType = cmbOrderType.getSelectedItem().toString();
            String paymentMethod = cmbPaymentMethod.getSelectedItem().toString();
            String paymentStatus = cmbPaymentStatus.getSelectedItem().toString();

            int billNumber = OrderManager.generateOrderNumber();

            Order order = new Order(
                    customer,
                    billNumber,
                    txtOrder.getText().trim(),
                    date,
                    txtTime.getText().trim(),
                    priceVal,
                    advanceVal,
                    paymentMethod,
                    paymentStatus,
                    orderType
            );

            OrderManager.addOrder(order);

            // Ask to send via WhatsApp
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "تم حفظ الطلب بنجاح! رقم الطلب: " + billNumber + "\nهل تريد إرسال الإشعار عبر الواتساب؟",
                    "نجاح الحفظ",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                WhatsAppSender.send(order);
            }

            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "حدث خطأ أثناء حفظ الطلب: " + e.getMessage(),
                    "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        resetFieldBorders();

        txtName.setText("");
        txtPhone.setText("");
        txtLocation.setText("");
        txtOrder.setText("");
        txtTime.setText("");
        txtPrice.setText("");
        txtAdvance.setText("");
        txtDue.setText("");

        cmbOrderType.setSelectedIndex(0);
        cmbPaymentMethod.setSelectedIndex(0);
        cmbPaymentStatus.setSelectedIndex(0);

        dateChooser.setDate(new Date());
    }
}