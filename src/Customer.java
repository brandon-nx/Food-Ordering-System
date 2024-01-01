import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String memberId;
    private String name;
    private String contactNumber;
    private String deliveryAddress;
    private List<Order> orderHistory;

    // Constructor
    public Customer(String memberId, String name, String contactNumber, String deliveryAddress) {
        this.memberId = memberId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.deliveryAddress = deliveryAddress;
        this.orderHistory = new ArrayList<>();
    }

    // Getters and Setters
    public String getMemberId(){
        return memberId;
    }

    public void setMemberId(String memberId){
        this.memberId = memberId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber){
        this.contactNumber = contactNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    public void setDeliveryAddress(String deliveryAddress){
        this.deliveryAddress = deliveryAddress;
    }
    public List<Order> getOrderHistory () {
        return orderHistory;
    }
    public void setOrderHistory(String deliveryAddress){
        this.orderHistory = orderHistory;
    }

    // Method to view order history
    public void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("-----------------------------------");
            System.out.println("No past orders found.");
            System.out.println("-----------------------------------");
            return;
        }

        System.out.println("-----------------------------------");
        System.out.println("Order History:");
        for (Order order : orderHistory) {
            System.out.println("Order at " + order.getRestaurant().getName() + " - Total Cost: $" + String.format("%.2f", order.getTotalCost()));
        }
        System.out.println("-----------------------------------");
    }

    // Method to serialise a Customer object
    public String serialise() {
        return memberId + "," + name + "," + contactNumber + "," + deliveryAddress;
    }

    // Static method to deserialise a string into a Customer object
    public static Customer deserialise(String data) {
        String[] parts = data.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid data format for deserialization");
        }
        return new Customer(parts[0], parts[1], parts[2], parts[3]);
    }
}
