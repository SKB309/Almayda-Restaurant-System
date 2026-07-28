import java.util.ArrayList;
import java.util.Stack;

public class OrderManager {

    private static ArrayList<Order> orders = new ArrayList<>();
    private static Stack<Order> deletedOrders = new Stack<>();
    private static int nextOrderNumber = 1001;

    public static int generateOrderNumber() {

        return nextOrderNumber++;

    }

    public static void addOrder(Order order) {
        orders.add(order);
    }

    public static ArrayList<Order> getOrders() {
        return orders;
    }

    public static Order findOrder(int billNumber) {

        for (Order order : orders) {

            if (order.getBillNumber() == billNumber) {
                return order;
            }

        }

        return null;
    }

    public static void deleteOrder(int billNumber) {

        Order order = findOrder(billNumber);

        if (order != null) {

            deletedOrders.push(order);
            orders.remove(order);

        }

    }

    public static boolean undoDelete() {

        if (!deletedOrders.isEmpty()) {

            orders.add(deletedOrders.pop());
            return true;

        }

        return false;
    }

    public static void updateOrder(Order updatedOrder) {

        for (int i = 0; i < orders.size(); i++) {

            if (orders.get(i).getBillNumber() == updatedOrder.getBillNumber()) {

                orders.set(i, updatedOrder);
                return;

            }

        }

    }

}