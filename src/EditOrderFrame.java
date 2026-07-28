import javax.swing.*;
import java.awt.*;

public class EditOrderFrame extends JFrame {


    JTextField txtName;
    JTextField txtPhone;
    JTextField txtLocation;

    JTextArea txtOrder;

    JTextField txtDate;
    JTextField txtTime;

    JTextField txtPrice;
    JTextField txtAdvance;
    JTextField txtDue;


    JComboBox<String> cmbOrderType;
    JComboBox<String> cmbPaymentMethod;
    JComboBox<String> cmbPaymentStatus;


    JButton btnUpdate;
    JButton btnCalculate;
    JButton btnCancel;


    int billNumber;



    public EditOrderFrame(int billNumber) {


        this.billNumber = billNumber;


        setTitle("Edit Order");

        setSize(600,800);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(new BorderLayout());




        JLabel title = new JLabel(
                "EDIT ORDER",
                SwingConstants.CENTER
        );


        title.setFont(
                new Font("Arial", Font.BOLD, 22)
        );


        add(title, BorderLayout.NORTH);






        JPanel form = new JPanel();

        form.setLayout(
                new GridLayout(0,2,10,10)
        );


        form.setBorder(
                BorderFactory.createEmptyBorder(
                        20,40,20,40
                )
        );





        txtName = new JTextField();

        txtPhone = new JTextField();

        txtLocation = new JTextField();


        txtOrder = new JTextArea(3,20);


        txtDate = new JTextField();

        txtTime = new JTextField();


        txtPrice = new JTextField();

        txtAdvance = new JTextField();


        txtDue = new JTextField();

        txtDue.setEditable(false);





        // Order Type

        cmbOrderType = new JComboBox<>(
                new String[]{
                        "Pickup",
                        "Delivery"
                }
        );



        // Payment Method

        cmbPaymentMethod = new JComboBox<>(
                new String[]{
                        "None",
                        "Cash",
                        "Card",
                        "Online"
                }
        );



        // Payment Status

        cmbPaymentStatus = new JComboBox<>(
                new String[]{
                        "Pending",
                        "Partially Paid",
                        "Paid"
                }
        );







        form.add(new JLabel("Customer Name"));
        form.add(txtName);



        form.add(new JLabel("Phone Number"));
        form.add(txtPhone);



        form.add(new JLabel("Location"));
        form.add(txtLocation);




        form.add(new JLabel("Order Details"));
        form.add(new JScrollPane(txtOrder));




        form.add(new JLabel("Date"));
        form.add(txtDate);



        form.add(new JLabel("Time"));
        form.add(txtTime);




        form.add(new JLabel("Total Price"));
        form.add(txtPrice);



        form.add(new JLabel("Advance"));
        form.add(txtAdvance);



        form.add(new JLabel("Due"));
        form.add(txtDue);




        form.add(new JLabel("Order Type"));
        form.add(cmbOrderType);



        form.add(new JLabel("Payment Method"));
        form.add(cmbPaymentMethod);



        form.add(new JLabel("Payment Status"));
        form.add(cmbPaymentStatus);





        add(form, BorderLayout.CENTER);






        JPanel buttons = new JPanel();



        btnCalculate =
                new JButton("Calculate Due");


        btnUpdate =
                new JButton("Update");


        btnCancel =
                new JButton("Cancel");



        buttons.add(btnCalculate);

        buttons.add(btnUpdate);

        buttons.add(btnCancel);



        add(buttons, BorderLayout.SOUTH);







        loadOrder();





        btnCalculate.addActionListener(e -> {

            calculateDue();

        });




        btnUpdate.addActionListener(e -> {

            updateOrder();

        });




        btnCancel.addActionListener(e -> {

            dispose();

        });





        setVisible(true);


    }







    // =========================
    // LOAD ORDER
    // =========================


    private void loadOrder() {



        Order order =
                OrderManager.findOrder(billNumber);



        if(order == null) {


            JOptionPane.showMessageDialog(
                    this,
                    "Order not found"
            );


            dispose();

            return;

        }





        Customer customer =
                order.getCustomer();




        txtName.setText(
                customer.getCustomerName()
        );


        txtPhone.setText(
                customer.getPhoneNumber()
        );


        txtLocation.setText(
                customer.getLocation()
        );


        txtOrder.setText(
                order.getOrderDetails()
        );


        txtDate.setText(
                order.getOrderDate()
        );


        txtTime.setText(
                order.getOrderTime()
        );


        txtPrice.setText(
                String.valueOf(
                        order.getTotalPrice()
                )
        );


        txtAdvance.setText(
                String.valueOf(
                        order.getAdvancePayment()
                )
        );


        txtDue.setText(
                String.valueOf(
                        order.getDueAmount()
                )
        );



        cmbOrderType.setSelectedItem(
                order.getOrderType()
        );


        cmbPaymentMethod.setSelectedItem(
                order.getPaymentMethod()
        );


        cmbPaymentStatus.setSelectedItem(
                order.getPaymentStatus()
        );


    }








    // =========================
    // CALCULATE DUE
    // =========================


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
                    "Enter valid numbers"
            );


        }


    }









    // =========================
    // UPDATE ORDER
    // =========================


    private void updateOrder() {



        try {



            Customer customer =

                    new Customer(
                            txtName.getText(),
                            txtPhone.getText(),
                            txtLocation.getText()
                    );





            Order updatedOrder =

                    new Order(

                            customer,

                            billNumber,

                            txtOrder.getText(),

                            txtDate.getText(),

                            txtTime.getText(),

                            Double.parseDouble(
                                    txtPrice.getText()
                            ),

                            Double.parseDouble(
                                    txtAdvance.getText()
                            ),

                            cmbPaymentMethod
                                    .getSelectedItem()
                                    .toString(),

                            cmbPaymentStatus
                                    .getSelectedItem()
                                    .toString(),

                            cmbOrderType
                                    .getSelectedItem()
                                    .toString()

                    );






            OrderManager.updateOrder(
                    updatedOrder
            );





            JOptionPane.showMessageDialog(
                    this,
                    "Order Updated Successfully"
            );



            dispose();



        }
        catch(Exception e){


            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );


        }



    }



}