import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        // Start Swing application
        SwingUtilities.invokeLater(() -> {

            new DashboardFrame();

        });

    }
}