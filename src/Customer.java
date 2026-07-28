public class Customer {

    private String customerName;
    private String phoneNumber;
    private String location;


    public Customer(String customerName, String phoneNumber, String location) {

        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.location = location;

    }


    // Getter for name
    public String getCustomerName() {

        return customerName;

    }


    // Getter for phone
    public String getPhoneNumber() {

        return phoneNumber;

    }


    // Getter for location
    public String getLocation() {

        return location;

    }


    // Setters

    public void setCustomerName(String customerName) {

        this.customerName = customerName;

    }


    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;

    }


    public void setLocation(String location) {

        this.location = location;

    }

}