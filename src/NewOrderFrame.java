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


    JButton btnSave;
    JButton btnCalculate;
    JButton btnClear;
    JButton btnBack;



    public NewOrderFrame() {


        setTitle("New Order");
        setSize(550,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0,2,10,10));



        // ======================
        // Customer Information
        // ======================


        panel.add(new JLabel("Customer Name"));

        txtName = new JTextField();

        panel.add(txtName);



        panel.add(new JLabel("Phone Number"));

        txtPhone = new JTextField();

        panel.add(txtPhone);



        panel.add(new JLabel("Location"));

        txtLocation = new JTextField();

        panel.add(txtLocation);




        // ======================
        // Order Details
        // ======================


        panel.add(new JLabel("Order Details"));

        txtOrder = new JTextField();

        panel.add(txtOrder);




        // ======================
        // Date Calendar
        // ======================


        panel.add(new JLabel("Order Date"));


        dateChooser = new JDateChooser();

        dateChooser.setDate(new Date());

        dateChooser.setDateFormatString(
                "EEEE، dd-MM-yyyy"
        );


        panel.add(dateChooser);




        // ======================
        // Time
        // ======================


        panel.add(new JLabel("Time"));

        txtTime = new JTextField();

        panel.add(txtTime);




        // ======================
        // Payment
        // ======================


        panel.add(new JLabel("Total Price"));

        txtPrice = new JTextField();

        panel.add(txtPrice);




        panel.add(new JLabel("Advance"));

        txtAdvance = new JTextField();

        panel.add(txtAdvance);




        panel.add(new JLabel("Due"));

        txtDue = new JTextField();

        txtDue.setEditable(false);

        panel.add(txtDue);




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
        // Button Actions
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


            if (!txtName.getText().isEmpty()
                    || !txtOrder.getText().isEmpty()
                    || !txtPrice.getText().isEmpty()) {


                int result = JOptionPane.showConfirmDialog(
                        this,
                        "There is an unsaved order. Save before leaving?",
                        "Unsaved Order",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );


                if (result == JOptionPane.YES_OPTION) {

                    saveOrder();

                }
                else if (result == JOptionPane.NO_OPTION) {

                    dispose();

                }


            } else {

                dispose();

            }


        });




        setVisible(true);

    }







    // ======================
    // Calculate Due
    // ======================


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


            double due =
                    price - advance;



            txtDue.setText(
                    String.valueOf(due)
            );



        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid amount"
            );


        }


    }








    // ======================
    // Save Order
    // ======================


    private void saveOrder() {



        try {



            String date =

                    new SimpleDateFormat(
                            "EEEE، dd-MM-yyyy",
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
                            advance
                    );





            OrderManager.addOrder(order);




            JOptionPane.showMessageDialog(
                    this,
                    "Order Saved\nOrder No: "
                            + billNumber
            );



            clearFields();




        }
        catch(Exception e){



            JOptionPane.showMessageDialog(
                    this,
                    "Error: "
                            + e.getMessage()
            );


        }



    }








    // ======================
    // Clear Fields
    // ======================


    private void clearFields(){


        txtName.setText("");

        txtPhone.setText("");

        txtLocation.setText("");

        txtOrder.setText("");

        txtTime.setText("");

        txtPrice.setText("");

        txtAdvance.setText("");

        txtDue.setText("");

        dateChooser.setDate(
                new Date()
        );


    }



}