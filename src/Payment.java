public class Payment {

    private double totalPrice;
    private double advancePayment;
    private double dueAmount;


    // Default Constructor
    public Payment() {
    }


    // Constructor
    public Payment(double totalPrice, double advancePayment) {

        this.totalPrice = totalPrice;
        this.advancePayment = advancePayment;
        calculateDue();

    }


    // Calculate remaining amount
    public void calculateDue() {

        this.dueAmount = totalPrice - advancePayment;

    }


    // Getters

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

    public void setTotalPrice(double totalPrice) {

        this.totalPrice = totalPrice;
        calculateDue();

    }


    public void setAdvancePayment(double advancePayment) {

        this.advancePayment = advancePayment;
        calculateDue();

    }


    public void setDueAmount(double dueAmount) {

        this.dueAmount = dueAmount;

    }


    @Override
    public String toString() {

        return "Payment Details\n" +
                "-------------------\n" +
                "Total Price     : " + totalPrice + "\n" +
                "Advance Payment : " + advancePayment + "\n" +
                "Due Amount      : " + dueAmount;

    }
}