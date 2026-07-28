import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DeliveryFrame extends JFrame {


    JTable table;

    DefaultTableModel model;


    JComboBox<String> cmbDriver;
    JComboBox<String> cmbStatus;


    JButton btnAssign;
    JButton btnTaken;
    JButton btnReturned;
    JButton btnDelivered;
    JButton btnRefresh;
    JButton btnBack;




    public DeliveryFrame() {


        setTitle("Delivery Management");

        setSize(1100,600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );


        setLayout(new BorderLayout());




        String[] columns = {

                "Bill No",
                "Customer",
                "Phone",
                "Location",
                "Order",
                "Amount",
                "Driver",
                "Status",
                "Taken Time"

        };



        model =
                new DefaultTableModel(
                        columns,
                        0
                );



        table =
                new JTable(model);



        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );







        JPanel bottom = new JPanel();




        cmbDriver = new JComboBox<>();


        for(Driver driver :
                DriverManager.getDrivers()) {


            cmbDriver.addItem(
                    driver.getName()
            );


        }






        cmbStatus =
                new JComboBox<>(
                        new String[]{

                                "Waiting",
                                "Taken",
                                "Returned",
                                "Delivered"

                        }
                );







        btnAssign =
                new JButton("Assign Driver");


        btnTaken =
                new JButton("Taken");


        btnReturned =
                new JButton("Returned");


        btnDelivered =
                new JButton("Delivered");


        btnRefresh =
                new JButton("Refresh");


        btnBack =
                new JButton("Back");





        bottom.add(
                new JLabel("Driver:")
        );


        bottom.add(cmbDriver);



        bottom.add(
                new JLabel("Status:")
        );


        bottom.add(cmbStatus);



        bottom.add(btnAssign);

        bottom.add(btnTaken);

        bottom.add(btnReturned);

        bottom.add(btnDelivered);

        bottom.add(btnRefresh);

        bottom.add(btnBack);




        add(
                bottom,
                BorderLayout.SOUTH
        );






        loadDeliveryOrders();






        btnAssign.addActionListener(e -> {

            assignDriver();

        });





        btnTaken.addActionListener(e -> {

            updateStatus("Taken");

        });





        btnReturned.addActionListener(e -> {

            updateStatus("Returned");

        });





        btnDelivered.addActionListener(e -> {

            updateStatus("Delivered");

        });





        btnRefresh.addActionListener(e -> {

            loadDeliveryOrders();

        });





        btnBack.addActionListener(e -> {

            dispose();

        });





        setVisible(true);

    }









    private void loadDeliveryOrders() {


        model.setRowCount(0);



        for(Order order :
                OrderManager.getOrders()) {




            /*
             Show:
             - Delivery orders
             - Old orders without Order Type
            */


            if(order.getOrderType() == null ||
                    order.getOrderType().equals("Delivery")) {




                Customer customer =
                        order.getCustomer();





                String driver =
                        order.getAssignedDriver();



                if(driver == null) {

                    driver =
                            "Not Assigned";

                }





                String status =
                        order.getDeliveryStatus();



                if(status == null) {

                    status =
                            "Waiting";

                }






                String takeTime =
                        order.getDriverTakeTime();



                if(takeTime == null) {

                    takeTime =
                            "-";

                }






                model.addRow(
                        new Object[]{


                                order.getBillNumber(),


                                customer.getCustomerName(),


                                customer.getPhoneNumber(),


                                customer.getLocation(),


                                order.getOrderDetails(),


                                order.getTotalPrice(),


                                driver,


                                status,


                                takeTime


                        }
                );



            }



        }



    }









    private void assignDriver() {


        int row =
                table.getSelectedRow();




        if(row == -1) {


            JOptionPane.showMessageDialog(
                    this,
                    "Select order first"
            );


            return;

        }






        int billNo =
                Integer.parseInt(
                        table.getValueAt(row,0)
                                .toString()
                );






        Order order =
                OrderManager.findOrder(
                        billNo
                );





        order.setAssignedDriver(

                cmbDriver
                        .getSelectedItem()
                        .toString()

        );





        order.setDeliveryStatus(
                "Taken"
        );





        order.setDriverTakeTime(

                new SimpleDateFormat(
                        "HH:mm"
                )
                        .format(
                                new Date()
                        )

        );





        JOptionPane.showMessageDialog(
                this,
                "Driver Assigned"
        );



        loadDeliveryOrders();



    }









    private void updateStatus(
            String status
    ) {


        int row =
                table.getSelectedRow();




        if(row == -1) {


            JOptionPane.showMessageDialog(
                    this,
                    "Select order first"
            );


            return;

        }






        int billNo =
                Integer.parseInt(
                        table.getValueAt(row,0)
                                .toString()
                );





        Order order =
                OrderManager.findOrder(
                        billNo
                );






        order.setDeliveryStatus(
                status
        );






        if(status.equals("Delivered")) {


            order.setDeliveryCompleteTime(

                    new SimpleDateFormat(
                            "HH:mm"
                    )
                            .format(
                                    new Date()
                            )

            );


        }






        loadDeliveryOrders();


    }


}