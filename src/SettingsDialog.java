import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SettingsDialog extends JDialog {

    private DashboardFrame parentFrame;
    private CateringPriceManager priceManager;
    private CardLayout cardLayout;
    private JPanel containerPanel;

    private Map<String, JTextField> proteinPriceFieldsMap;
    private Map<String, JTextField> proteinCookingFeeFieldsMap;

    public SettingsDialog(DashboardFrame owner, CateringPriceManager priceManager, String initialCard) {
        super(owner, "System Settings", true);
        this.parentFrame = owner;
        this.priceManager = priceManager;

        setSize(480, 680);
        setLocationRelativeTo(owner);
        setResizable(false);

        proteinPriceFieldsMap = new HashMap<>();
        proteinCookingFeeFieldsMap = new HashMap<>();

        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // ==========================================
        // 1. SETTINGS MAIN MENU CARD (Uniform Layout)
        // ==========================================
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(25, 45, 25, 45));

        Dimension buttonSize = new Dimension(390, 45);

        JButton btnMenuPricing = createStyledButton("💰 Pricing & Cooking Fees", buttonSize);
        JButton btnMenuCustomMenu = createStyledButton("🍽️ Menu Options Settings", buttonSize);
        JButton btnMenuDrivers = createStyledButton("🚗 Drivers / Staff Management", buttonSize);

        // Uniform Theme Row Panel matching button dimensions
        JPanel themeRowPanel = new JPanel(new BorderLayout());
        themeRowPanel.setMaximumSize(buttonSize);
        themeRowPanel.setPreferredSize(buttonSize);
        themeRowPanel.setBorder(BorderFactory.createCompoundBorder(
                UIManager.getBorder("Button.border"),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));
        themeRowPanel.setBackground(UIManager.getColor("Button.background"));

        JLabel lblTheme = new JLabel("Theme:");
        lblTheme.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JToggleButton btnThemeToggle = new JToggleButton("🌙 Dark Mode");
        btnThemeToggle.setPreferredSize(new Dimension(140, 32));
        btnThemeToggle.setFocusPainted(false);

        btnThemeToggle.addActionListener(e -> {
            if (btnThemeToggle.isSelected()) {
                btnThemeToggle.setText("☀️ Light Mode");
                Main.switchTheme(true);
            } else {
                btnThemeToggle.setText("🌙 Dark Mode");
                Main.switchTheme(false);
            }
        });

        themeRowPanel.add(lblTheme, BorderLayout.WEST);
        themeRowPanel.add(btnThemeToggle, BorderLayout.EAST);

        JButton btnMenuClose = createStyledButton("⬅ Back to Dashboard", buttonSize);

        menuPanel.add(btnMenuPricing);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnMenuCustomMenu);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnMenuDrivers);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(themeRowPanel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnMenuClose);

        // ==========================================
        // 2. PRICING SETTINGS CARD (Ingredients + Cooking Fees)
        // ==========================================
        JPanel pricePanel = new JPanel(new GridLayout(0, 2, 10, 10));
        pricePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtRice = new JTextField(String.valueOf(priceManager.getRicePricePerMun()));
        pricePanel.add(new JLabel("سعر العيش (Rice/Mun):"));
        pricePanel.add(txtRice);

        // Dynamic Protein Prices (Mun-based, excluding mutton which uses fixed sizes)
        for (String protein : priceManager.getProteinTypes()) {
            if (protein.equals("ضأن")) continue;
            pricePanel.add(new JLabel("سعر " + protein + " (Mun):"));
            JTextField tf = new JTextField(String.valueOf(priceManager.getProteinPrice(protein)));
            proteinPriceFieldsMap.put(protein, tf);
            pricePanel.add(tf);
        }

        // Mutton Sizes
        JTextField txtMuttonFull = new JTextField(String.valueOf(priceManager.getMuttonFullPrice()));
        JTextField txtMuttonHalf = new JTextField(String.valueOf(priceManager.getMuttonHalfPrice()));
        JTextField txtMuttonQuarter = new JTextField(String.valueOf(priceManager.getMuttonQuarterPrice()));

        pricePanel.add(new JLabel("سعر الضأن (Full Mutton):"));
        pricePanel.add(txtMuttonFull);
        pricePanel.add(new JLabel("سعر الضأن (Half Mutton):"));
        pricePanel.add(txtMuttonHalf);
        pricePanel.add(new JLabel("سعر الضأن (Quarter Mutton):"));
        pricePanel.add(txtMuttonQuarter);

        // Dynamic Protein Cooking Fees
        for (String protein : priceManager.getProteinTypes()) {
            pricePanel.add(new JLabel("رسوم طبخ " + protein + " (Cooking Fee):"));
            JTextField tf = new JTextField(String.valueOf(priceManager.getProteinCookingFee(protein)));
            proteinCookingFeeFieldsMap.put(protein, tf);
            pricePanel.add(tf);
        }

        JButton btnSavePrices = new JButton("Save Prices");
        btnSavePrices.addActionListener(e -> {
            try {
                priceManager.setRicePricePerMun(Double.parseDouble(txtRice.getText().trim()));

                // Save dynamic protein prices
                for (Map.Entry<String, JTextField> entry : proteinPriceFieldsMap.entrySet()) {
                    priceManager.setProteinPrice(entry.getKey(), Double.parseDouble(entry.getValue().getText().trim()));
                }

                priceManager.setMuttonFullPrice(Double.parseDouble(txtMuttonFull.getText().trim()));
                priceManager.setMuttonHalfPrice(Double.parseDouble(txtMuttonHalf.getText().trim()));
                priceManager.setMuttonQuarterPrice(Double.parseDouble(txtMuttonQuarter.getText().trim()));

                // Save dynamic cooking fees
                for (Map.Entry<String, JTextField> entry : proteinCookingFeeFieldsMap.entrySet()) {
                    priceManager.setProteinCookingFee(entry.getKey(), Double.parseDouble(entry.getValue().getText().trim()));
                }

                JOptionPane.showMessageDialog(this, "تم تحديث الأسعار ورسوم الطبخ بنجاح!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "يرجى إدخال أرقام صحيحة أو عشرية صحيحة.", "خطأ في الإدخال", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnBackFromPrice = new JButton("⬅ Back to Settings");
        btnBackFromPrice.addActionListener(e -> cardLayout.show(containerPanel, "MENU"));

        JPanel priceBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        priceBottomPanel.add(btnBackFromPrice);
        priceBottomPanel.add(btnSavePrices);

        JPanel priceContainer = new JPanel(new BorderLayout());
        priceContainer.add(new JScrollPane(pricePanel), BorderLayout.CENTER);
        priceContainer.add(priceBottomPanel, BorderLayout.SOUTH);

        // ==========================================
        // 3. MENU OPTIONS SETTINGS CARD (Rice, Proteins & Dishes)
        // ==========================================
        JPanel menuOptionsPanel = new JPanel();
        menuOptionsPanel.setLayout(new BoxLayout(menuOptionsPanel, BoxLayout.Y_AXIS));
        menuOptionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtRiceTypes = new JTextField(String.join(", ", priceManager.getRiceTypes()));
        JTextField txtProteinTypes = new JTextField(String.join(", ", priceManager.getProteinTypes()));
        JTextField txtBeefChickenTypes = new JTextField(String.join(", ", priceManager.getBeefChickenDishTypes()));
        JTextField txtFishTypes = new JTextField(String.join(", ", priceManager.getFishDishTypes()));

        menuOptionsPanel.add(new JLabel("أنواع العيش (مفصولة بفاصلة):"));
        menuOptionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuOptionsPanel.add(txtRiceTypes);
        menuOptionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        menuOptionsPanel.add(new JLabel("أنواع البروتين (مفصولة بفاصلة):"));
        menuOptionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuOptionsPanel.add(txtProteinTypes);
        menuOptionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        menuOptionsPanel.add(new JLabel("أنواع أطباق البقر والدجاج (مفصولة بفاصلة):"));
        menuOptionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuOptionsPanel.add(txtBeefChickenTypes);
        menuOptionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        menuOptionsPanel.add(new JLabel("أنواع أطباق السمك (مفصولة بفاصلة):"));
        menuOptionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuOptionsPanel.add(txtFishTypes);
        menuOptionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel lblMenuNote = new JLabel("<html><b>ملاحظة:</b> هريس، عرسية، وثريد متاحة للبقر والدجاج والضأن.</html>");
        lblMenuNote.setForeground(new Color(100, 100, 100));
        menuOptionsPanel.add(lblMenuNote);

        JButton btnSaveMenuOptions = new JButton("Save Menu Options");
        btnSaveMenuOptions.addActionListener(e -> {
            List<String> updatedRice = Arrays.stream(txtRiceTypes.getText().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            List<String> updatedProtein = Arrays.stream(txtProteinTypes.getText().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            List<String> updatedBeefChicken = Arrays.stream(txtBeefChickenTypes.getText().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            List<String> updatedFish = Arrays.stream(txtFishTypes.getText().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            priceManager.setRiceTypes(updatedRice);
            priceManager.setProteinTypes(updatedProtein);
            priceManager.setBeefChickenDishTypes(updatedBeefChicken);
            priceManager.setFishDishTypes(updatedFish);

            JOptionPane.showMessageDialog(this, "تم تحديث خيارات القائمة بنجاح!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnBackFromMenuOptions = new JButton("⬅ Back to Settings");
        btnBackFromMenuOptions.addActionListener(e -> cardLayout.show(containerPanel, "MENU"));

        JPanel menuOptionsBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        menuOptionsBottomPanel.add(btnBackFromMenuOptions);
        menuOptionsBottomPanel.add(btnSaveMenuOptions);

        JPanel menuOptionsContainer = new JPanel(new BorderLayout());
        menuOptionsContainer.add(new JScrollPane(menuOptionsPanel), BorderLayout.CENTER);
        menuOptionsContainer.add(menuOptionsBottomPanel, BorderLayout.SOUTH);

        // ==========================================
        // ADD CARDS TO CONTAINER
        // ==========================================
        containerPanel.add(menuPanel, "MENU");
        containerPanel.add(priceContainer, "PRICING");
        containerPanel.add(menuOptionsContainer, "MENU_OPTIONS");

        // Button Listeners
        btnMenuPricing.addActionListener(e -> cardLayout.show(containerPanel, "PRICING"));
        btnMenuCustomMenu.addActionListener(e -> cardLayout.show(containerPanel, "MENU_OPTIONS"));
        btnMenuDrivers.addActionListener(e -> {
            dispose();
            openDriverSubScreen();
        });
        btnMenuClose.addActionListener(e -> dispose());

        helperAlign(menuPanel);

        cardLayout.show(containerPanel, initialCard);
        add(containerPanel);
    }

    private JButton createStyledButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        return button;
    }

    private void helperAlign(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JComponent) {
                ((JComponent) comp).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }
    }

    private void openDriverSubScreen() {
        parentFrame.setVisible(false);
        DriverFrame driverFrame = new DriverFrame();

        driverFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentFrame.setVisible(true);
                new SettingsDialog(parentFrame, priceManager, "MENU").setVisible(true);
            }
        });

        driverFrame.setVisible(true);
    }
}