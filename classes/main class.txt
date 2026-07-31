import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // 1. Enable custom window title bars decorated by FlatLaf (Optional, looks very modern)
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        // 2. Initialize the Look and Feel
        try {
            // Recommended FlatLaf setup method
            FlatLightLaf.setup();

            // Or if you prefer dark mode by default:
            // FlatDarkLaf.setup();
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf UI theme: " + ex.getMessage());
        }

        // 3. Launch Swing Application safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new DashboardFrame();
        });
    }

    /**
     * Helper method to switch themes at runtime from anywhere in the app
     * (e.g., from a button in DashboardFrame)
     */
    public static void switchTheme(boolean isDark) {
        try {
            if (isDark) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            // Update UI for all open frames immediately
            FlatLaf.updateUI();
        } catch (Exception ex) {
            System.err.println("Failed to switch theme: " + ex.getMessage());
        }
    }
}