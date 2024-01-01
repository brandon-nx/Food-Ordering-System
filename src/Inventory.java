import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<MenuItem, Integer> stock;

    // Constructor
    public Inventory() {
        this.stock = new HashMap<>();
    }

    // Method to update the inventory
    public void updateInventory(MenuItem item, int quantity) {
        int currentStock = stock.getOrDefault(item, 0);
        int newStock = currentStock + quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("Cannot have negative stock.");
        }
        stock.put(item, newStock);
    }

    // Method to check the item availability
    public int checkAvailability(MenuItem item) {
        // Return the quantity available for the item
        return stock.getOrDefault(item, 0);
    }
}
