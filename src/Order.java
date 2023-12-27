import java.util.ArrayList;
import java.util.List;

class Order {
    private final User user;
    private final Restaurant restaurant;
    private final List<CartItem> items;
    private double totalCost;
    private List<SpecialOffer> applicableOffers;


    // Constructor with special offers
    public Order(User user, Restaurant restaurant, List<CartItem> items, List<SpecialOffer> offers) {
        this.user = user;
        this.restaurant = restaurant;
        this.items = items;
        this.applicableOffers = offers;
        this.totalCost = calculateTotalCost();
    }

    // Overloaded constructor without special offers
    public Order(User user, Restaurant restaurant, List<CartItem> items) {
        this(user, restaurant, items, new ArrayList<>()); // Calls the main constructor with an empty list of offers
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

    public double getTotalCost() {
        return totalCost;
    }

    public void confirmOrder() {
        // Additional logic for confirming the order
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public List<CartItem> getItems() {
        return items;
    }
}