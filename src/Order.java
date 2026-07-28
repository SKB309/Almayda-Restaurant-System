public class Order {

    // Customer Information
    private Customer customer;

    // Order Information
    private int billNumber;
    private String orderDetails;
    private String orderDate;
    private String orderTime;

    // Payment Information
    private double totalPrice;
    private double advancePayment;
    private double dueAmount;

    // Default Constructor
    public Order() {
    }

    // Parameterized Constructor
    public Order(Customer customer, int billNumber, String orderDetails,
                 String orderDate, String orderTime,
                 double totalPrice, double advancePayment) {

        this.customer = customer;
        this.billNumber = billNumber;
        this.orderDetails = orderDetails;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
        this.advancePayment = advancePayment;
        this.dueAmount = totalPrice - advancePayment;
    }

    // Getters
    public Customer getCustomer() {
        return customer;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getAdvancePayment() {
        return advancePayment;
    }

    public double getDueAmount() {
        return dueAmount;
    }

    // Setters
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        calculateDue();
    }

    public void setAdvancePayment(double advancePayment) {
        this.advancePayment = advancePayment;
        calculateDue();
    }

    // Calculate Remaining Due
    public void calculateDue() {
        this.dueAmount = totalPrice - advancePayment;
    }

    @Override
    public String toString() {
        return "========== ORDER ==========\n" +
                customer +
                "\n\nBill Number : " + billNumber +
                "\nOrder Date  : " + orderDate +
                "\nOrder Time  : " + orderTime +
                "\nOrder Items : " + orderDetails +
                "\nTotal Price : " + totalPrice +
                "\nAdvance     : " + advancePayment +
                "\nDue Amount  : " + dueAmount +
                "\n===========================";
    }
}