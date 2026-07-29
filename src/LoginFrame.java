import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Almayda - Login");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        add(loginButton, gbc);

        // Action Listener
        loginButton.addActionListener(e -> authenticate());

        setVisible(true);
    }

    private void authenticate() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        // Simple check (Can be expanded with user roles later)
        if (user.equals("admin") && pass.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            this.dispose(); // Close login window
            new DashboardFrame(); // Open Dashboard
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}