import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    // Constructor
    public Cart() {
        this.items = new ArrayList<>();
    }

    // Getters and Setters
    public List<CartItem> getItems() {
        return items;
    }

    // Method to add item into cart
    public void addItem(CartItem cartItem) {
        items.add(cartItem);
    }

    // Method to remove item from cart
    public void removeItem(CartItem cartItem) {
        items.remove(cartItem);
    }

    // Method to calculate total cost in the cart
    public double calculateTotalCost() {
        double totalCost = 0.0;
        for (CartItem cartItem : items) {
            totalCost += cartItem.getMenuItem().getPrice() * cartItem.getQuantity();
        }
        return totalCost;
    }

    // Method to display the cart contents
    public void displayCartContents() {
        if (items.isEmpty()) {
            System.out.println("-----------------------------------");
            System.out.println("Your cart is empty.");
            System.out.println("-----------------------------------");
            return;
        }

        System.out.println("-----------------------------------");
        System.out.println("Cart Contents:");
        for (CartItem item : items) {
            String formattedPrice = String.format("%.2f", item.getMenuItem().getPrice() * item.getQuantity());
            System.out.println(item.getQuantity() + " x " + item.getMenuItem().getName() + " - $" + formattedPrice);
        }
        System.out.println("Total Cost: $" + String.format("%.2f", calculateTotalCost()));
        System.out.println("-----------------------------------");
    }
}
