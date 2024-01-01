import java.util.List;

public class Order {
    private Customer customer;
    private Restaurant restaurant;
    private List<CartItem> items;
    private double totalCost;
    private List<SpecialOffer> applicableOffers;

    // Constructor with special offers
    public Order(Customer customer, Restaurant restaurant, List<CartItem> items, List<SpecialOffer> offers) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
        this.applicableOffers = offers;
        this.totalCost = calculateTotalCost();
    }

    // Getter and Setters
    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public List<CartItem> getItems() {
        return items;
    }
    public double getTotalCost() {
        return totalCost;
    }

    // Method to calculate and update the total cost
    private double calculateTotalCost() {
        double total = 0.0;
        for (CartItem cartItem : items) {
            total += cartItem.getMenuItem().getPrice() * cartItem.getQuantity();
        }

        for (SpecialOffer offer : applicableOffers) {
            total = offer.applyOffer(total);
        }

        this.totalCost = total;
        return total;
    }

    // Method to confirm order
    public void confirmOrder() {
        // Additional logic for confirming the order
    }
}
