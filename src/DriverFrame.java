import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DriverFrame extends JFrame {


    JTable table;

    DefaultTableModel model;


    JTextField txtName;
    JTextField txtPhone;


    JButton btnAdd;
    JButton btnDelete;
    JButton btnClear;
    JButton btnBack;




    public DriverFrame(){


        setTitle("Driver Management");

        setSize(600,450);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );


        setLayout(new BorderLayout());





        // ======================
        // TABLE
        // ======================


        model = new DefaultTableModel(

                new String[]{
                        "ID",
                        "Driver Name",
                        "Phone"
                },

                0

        );



        table = new JTable(model);



        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );






        // ======================
        // INPUT PANEL
        // ======================


        JPanel input = new JPanel();


        input.add(
                new JLabel("Name")
        );


        txtName = new JTextField(10);

        input.add(txtName);




        input.add(
                new JLabel("Phone")
        );


        txtPhone = new JTextField(10);


        input.add(txtPhone);





        add(
                input,
                BorderLayout.NORTH
        );






        // ======================
        // BUTTONS
        // ======================


        JPanel buttons = new JPanel();



        btnAdd =
                new JButton("Add Driver");


        btnDelete =
                new JButton("Delete");


        btnClear =
                new JButton("Clear");


        btnBack =
                new JButton("Back");





        buttons.add(btnAdd);

        buttons.add(btnDelete);

        buttons.add(btnClear);

        buttons.add(btnBack);




        add(
                buttons,
                BorderLayout.SOUTH
        );






        loadDrivers();







        btnAdd.addActionListener(e -> {


            addDriver();


        });







        btnDelete.addActionListener(e -> {


            deleteDriver();


        });







        btnClear.addActionListener(e -> {


            txtName.setText("");

            txtPhone.setText("");

        });







        btnBack.addActionListener(e -> {


            dispose();


        });





        setVisible(true);


    }









    private void loadDrivers(){


        model.setRowCount(0);



        for(Driver d :
                DriverManager.getDrivers()) {



            model.addRow(
                    new Object[]{

                            d.getId(),

                            d.getName(),

                            d.getPhone()

                    }
            );


        }


    }










    private void addDriver(){


        if(txtName.getText().isEmpty()
                ||
                txtPhone.getText().isEmpty()){


            JOptionPane.showMessageDialog(
                    this,
                    "Enter driver information"
            );


            return;

        }






        int id =
                DriverManager
                        .getDrivers()
                        .size()
                        + 1;






        Driver driver =
                new Driver(

                        id,

                        txtName.getText(),

                        txtPhone.getText()

                );






        DriverManager.addDriver(driver);




        loadDrivers();




        txtName.setText("");

        txtPhone.setText("");




        JOptionPane.showMessageDialog(
                this,
                "Driver Added"
        );


    }









    private void deleteDriver(){



        int row =
                table.getSelectedRow();



        if(row == -1){


            JOptionPane.showMessageDialog(
                    this,
                    "Select driver first"
            );


            return;


        }






        int id =
                Integer.parseInt(
                        table.getValueAt(row,0)
                                .toString()
                );






        DriverManager.deleteDriver(id);




        loadDrivers();



    }



}