import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // 1. Set the modern Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            //UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize modern UI theme.");
        }

        // 2. Start Swing application
        SwingUtilities.invokeLater(() -> {
            new DashboardFrame();
        });
    }
}