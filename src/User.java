import java.util.ArrayList;
import java.util.List;

class User {
    private final String name;
    private final String contactInfo;
    private final String deliveryAddress;
    private List<Order> orderHistory; // To store the history of orders

    public User(String name, String contactInfo, String deliveryAddress) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.deliveryAddress = deliveryAddress;
        this.orderHistory = new ArrayList<>(); // Initialize the order history list
    }

    // Getter methods for name, contactInfo, and deliveryAddress
    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }


    public void placeOrder(Restaurant restaurant, Cart cart) {
        restaurant.processOrder(this, cart); // Pass the current user and the cart
        Order newOrder = new Order(this, restaurant, cart.getItems()); // Create a new order
        orderHistory.add(newOrder); // Add the new order to the history
    }


    // Method to view order history
    public void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("No past orders found.");
            return;
        }
        System.out.println("Order History:");
        for (Order order : orderHistory) {
            System.out.println("Order at " + order.getRestaurant().getName() + " - Total Cost: $" + String.format("%.2f", order.getTotalCost()));
            // You can add more details about each order if needed
        }
    }
}