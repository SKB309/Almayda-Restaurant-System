import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class WhatsAppSender {


    public static void send(Order order) {


        try {


            String phone =
                    order.getCustomer().getPhoneNumber();


            // Remove spaces and symbols
            phone = phone.replaceAll("\\D", "");



            String message =

                    "🍽️ مطبخ المائدة العمانية 🍽️\n\n" +

                            "اسم العميل: " +
                            order.getCustomer().getCustomerName() +
                            "\n" +


                            "رقم الطلب: " +
                            order.getBillNumber() +
                            "\n\n" +



                            "رقم الهاتف: " +
                            order.getCustomer().getPhoneNumber() +
                            "\n" +

                            "الموقع: " +
                            order.getCustomer().getLocation() +
                            "\n\n" +


                            "تفاصيل الطلب:\n" +
                            order.getOrderDetails() +
                            "\n\n" +


                            "التاريخ: " +
                            order.getOrderDate() +
                            "\n" +

                            "الوقت: " +
                            order.getOrderTime() +
                            "\n\n" +


                            "الإجمالي: " +
                            order.getTotalPrice() +
                            " ر.ع\n" +

                            "الدفعة المقدمة: " +
                            order.getAdvancePayment() +
                            " ر.ع\n" +

                            "المبلغ المتبقي: " +
                            order.getDueAmount() +
                            " ر.ع\n\n" +


                            "شكراً لاختياركم مطبخ المائدة العمانية 🌹";



            String url =

                    "https://api.whatsapp.com/send?phone="
                            + phone
                            + "&text="
                            + URLEncoder.encode(
                            message,
                            StandardCharsets.UTF_8
                    );



            System.out.println(url);



            Desktop.getDesktop()
                    .browse(new URI(url));



        } catch (Exception e) {


            e.printStackTrace();


        }


    }

}