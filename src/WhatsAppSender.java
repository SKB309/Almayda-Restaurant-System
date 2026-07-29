import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WhatsAppSender {

    private static final String DEFAULT_COUNTRY_CODE = "968";

    public static void send(Order order) {
        if (order == null || order.getCustomer() == null) {
            JOptionPane.showMessageDialog(null,
                    "بيانات الطلب أو العميل غير مكتملة!",
                    "تنبيه",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String rawPhone = order.getCustomer().getPhoneNumber();
            if (rawPhone == null || rawPhone.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "رقم الهاتف غير موجود!",
                        "خطأ",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Remove non-digits
            String phone = rawPhone.replaceAll("\\D", "");

            // Prepend country code if 8 digits
            if (phone.length() == 8) {
                phone = DEFAULT_COUNTRY_CODE + phone;
            }

            // Message formatting using the updated safeString(Object)
            String message =
                    "🍽️ مطبخ المائدة العمانية 🍽️\n\n" +
                            "اسم العميل: " + safeString(order.getCustomer().getCustomerName()) + "\n" +
                            "رقم الطلب: " + safeString(order.getBillNumber()) + "\n\n" +
                            "رقم الهاتف: " + rawPhone + "\n" +
                            "الموقع: " + safeString(order.getCustomer().getLocation()) + "\n\n" +
                            "تفاصيل الطلب:\n" + safeString(order.getOrderDetails()) + "\n\n" +
                            "التاريخ: " + safeString(order.getOrderDate()) + "\n" +
                            "الوقت: " + safeString(order.getOrderTime()) + "\n\n" +
                            "الإجمالي: " + order.getTotalPrice() + " ر.ع\n" +
                            "الدفعة المقدمة: " + order.getAdvancePayment() + " ر.ع\n" +
                            "المبلغ المتبقي: " + order.getDueAmount() + " ر.ع\n\n" +
                            "شكراً لاختياركم مطبخ المائدة العمانية 🌹";

            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + encodedMessage;

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                JOptionPane.showMessageDialog(null,
                        "متصفح النظام غير مدعوم لفتح الواتساب.",
                        "خطأ",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "حدث خطأ أثناء فتح الواتساب: " + e.getMessage(),
                    "خطأ",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Accepts numbers, text, or dates safely
    private static String safeString(Object value) {
        return value != null ? String.valueOf(value) : "";
    }
}