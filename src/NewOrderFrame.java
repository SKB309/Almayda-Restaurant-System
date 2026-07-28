import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.toedter.calendar.JDateChooser;


public class NewOrderFrame extends JFrame {


    JTextField txtName;
    JTextField txtPhone;
    JTextField txtLocation;

    JTextField txtOrder;
    JTextField txtTime;

    JTextField txtPrice;
    JTextField txtAdvance;
    JTextField txtDue;


    JDateChooser dateChooser;


    JComboBox<String> cmbOrderType;
    JComboBox<String> cmbPaymentMethod;
    JComboBox<String> cmbPaymentStatus;



    JButton btnSave;
    JButton btnCalculate;
    JButton btnClear;
    JButton btnBack;





    public NewOrderFrame() {


        setTitle("New Order");

        setSize(550,750);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );



        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(0,2,10,10)
        );





        // ======================
        // Customer Information
        // ======================


        panel.add(
                new JLabel("Customer Name")
        );


        txtName = new JTextField();

        panel.add(txtName);




        panel.add(
                new JLabel("Phone Number")
        );


        txtPhone = new JTextField();

        panel.add(txtPhone);




        panel.add(
                new JLabel("Location")
        );


        txtLocation = new JTextField();

        panel.add(txtLocation);






        // ======================
        // Order Details
        // ======================


        panel.add(
                new JLabel("Order Details")
        );


        txtOrder = new JTextField();

        panel.add(txtOrder);






        // ======================
        // Order Type
        // ======================


        panel.add(
                new JLabel("Order Type")
        );


        cmbOrderType = new JComboBox<>(
                new String[]{
                        "Pickup",
                        "Delivery"
                }
        );


        panel.add(cmbOrderType);







        // ======================
        // Date
        // ======================


        panel.add(
                new JLabel("Order Date")
        );


        dateChooser =
                new JDateChooser();


        dateChooser.setDate(
                new Date()
        );


        dateChooser.setDateFormatString(
                "EEEE, dd-MM-yyyy"
        );


        panel.add(dateChooser);







        // ======================
        // Time
        // ======================


        panel.add(
                new JLabel("Time")
        );


        txtTime = new JTextField();


        panel.add(txtTime);








        // ======================
        // Payment
        // ======================


        panel.add(
                new JLabel("Total Price")
        );


        txtPrice = new JTextField();

        panel.add(txtPrice);




        panel.add(
                new JLabel("Advance")
        );


        txtAdvance = new JTextField();

        panel.add(txtAdvance);




        panel.add(
                new JLabel("Due")
        );


        txtDue = new JTextField();

        txtDue.setEditable(false);

        panel.add(txtDue);








        // ======================
        // Payment Method
        // ======================


        panel.add(
                new JLabel("Payment Method")
        );


        cmbPaymentMethod = new JComboBox<>(
                new String[]{
                        "None",
                        "Cash",
                        "Card",
                        "Online"
                }
        );


        panel.add(cmbPaymentMethod);








        // ======================
        // Payment Status
        // ======================


        panel.add(
                new JLabel("Payment Status")
        );


        cmbPaymentStatus = new JComboBox<>(
                new String[]{
                        "Pending",
                        "Partially Paid",
                        "Paid"
                }
        );


        panel.add(cmbPaymentStatus);









        // ======================
        // Buttons
        // ======================


        btnCalculate =
                new JButton("Calculate Due");


        btnSave =
                new JButton("Save Order");


        btnClear =
                new JButton("Clear");


        btnBack =
                new JButton("Back");



        panel.add(btnCalculate);

        panel.add(btnSave);

        panel.add(btnClear);

        panel.add(btnBack);




        add(panel);







        // ======================
        // Actions
        // ======================


        btnCalculate.addActionListener(e -> {

            calculateDue();

        });





        btnSave.addActionListener(e -> {

            saveOrder();

        });





        btnClear.addActionListener(e -> {

            clearFields();

        });





        btnBack.addActionListener(e -> {


            dispose();


        });






        setVisible(true);


    }









    private void calculateDue() {


        try {


            double price =
                    Double.parseDouble(
                            txtPrice.getText()
                    );


            double advance =
                    Double.parseDouble(
                            txtAdvance.getText()
                    );



            txtDue.setText(
                    String.valueOf(
                            price - advance
                    )
            );


        }
        catch(Exception e) {


            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid amount"
            );


        }


    }









    private void saveOrder() {



        try {



            String date =

                    new SimpleDateFormat(
                            "EEEE, dd-MM-yyyy",
                            new Locale("ar")
                    )
                            .format(
                                    dateChooser.getDate()
                            );





            Customer customer =

                    new Customer(
                            txtName.getText(),
                            txtPhone.getText(),
                            txtLocation.getText()
                    );






            double price =

                    Double.parseDouble(
                            txtPrice.getText()
                    );





            double advance =

                    Double.parseDouble(
                            txtAdvance.getText()
                    );






            String orderType =

                    cmbOrderType
                            .getSelectedItem()
                            .toString();





            String paymentMethod =

                    cmbPaymentMethod
                            .getSelectedItem()
                            .toString();





            String paymentStatus =

                    cmbPaymentStatus
                            .getSelectedItem()
                            .toString();







            int billNumber =

                    OrderManager.generateOrderNumber();







            Order order =

                    new Order(

                            customer,

                            billNumber,

                            txtOrder.getText(),

                            date,

                            txtTime.getText(),

                            price,

                            advance,

                            paymentMethod,

                            paymentStatus,

                            orderType

                    );






            OrderManager.addOrder(order);






            JOptionPane.showMessageDialog(
                    this,
                    "Order Saved\nOrder No: "
                            + billNumber
            );



            clearFields();





        }
        catch(Exception e) {



            JOptionPane.showMessageDialog(
                    this,
                    "Error: "
                            + e.getMessage()
            );


        }


    }









    private void clearFields() {


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




        dateChooser.setDate(
                new Date()
        );


    }



}