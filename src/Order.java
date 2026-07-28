public class Order {

    // Customer Information
    private Customer customer;


    // Order Information
    private int billNumber;
    private String orderDetails;
    private String orderDate;
    private String orderTime;
    private String assignedDriver;
    private String deliveryStatus;
    private String driverTakeTime;
    private String deliveryCompleteTime;


    // Payment Information
    private double totalPrice;
    private double advancePayment;
    private double dueAmount;

    private String paymentMethod;
    private String paymentStatus;
    private String orderType;



    // Default Constructor

    public Order() {

        paymentMethod = "None";
        paymentStatus = "Pending";
        orderType = "Pickup";

        assignedDriver = "Not Assigned";
        deliveryStatus = "Waiting";

    }

    // Parameterized Constructor
    public Order(
                 Customer customer,
                 int billNumber,
                 String orderDetails,
                 String orderDate,
                 String orderTime,
                 double totalPrice,
                 double advancePayment,
                 String paymentMethod,
                 String paymentStatus,
                 String orderType)
     {


        this.customer = customer;
        this.billNumber = billNumber;
        this.orderDetails = orderDetails;
        this.orderDate = orderDate;
        this.orderTime = orderTime;

        this.totalPrice = totalPrice;
        this.advancePayment = advancePayment;

        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;

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



    public String getPaymentMethod() {
        return paymentMethod;
    }



    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getOrderType() {

        return orderType;

    }


    public String getAssignedDriver() {
        return assignedDriver;
    }

    public void setAssignedDriver(String assignedDriver) {
        this.assignedDriver = assignedDriver;
    }


    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }


    public String getDriverTakeTime() {
        return driverTakeTime;
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



    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }



    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setOrderType(String orderType) {

        this.orderType = orderType;

    }

    public void setDriverTakeTime(String driverTakeTime) {
        this.driverTakeTime = driverTakeTime;
    }


    public String getDeliveryCompleteTime() {
        return deliveryCompleteTime;
    }

    public void setDeliveryCompleteTime(String deliveryCompleteTime) {
        this.deliveryCompleteTime = deliveryCompleteTime;
    }



    // Calculate Remaining Due

    public void calculateDue() {

        this.dueAmount = totalPrice - advancePayment;

    }





    @Override
    public String toString() {

        return "========== ORDER ==========\n" +

                customer +

                "\n\nBill Number   : " + billNumber +
                "\nOrder Date    : " + orderDate +
                "\nOrder Time    : " + orderTime +
                "\nOrder Items   : " + orderDetails +

                "\nTotal Price   : " + totalPrice +
                "\nAdvance       : " + advancePayment +
                "\nDue Amount    : " + dueAmount +

                "\nPayment Method: " + paymentMethod +
                "\nPayment Status: " + paymentStatus +

                "\n===========================";

    }

}