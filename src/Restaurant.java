import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Restaurant {
    private String name;
    private List<MenuItem> menu;
    private List<SpecialOffer> specialOffers;
    private Inventory inventory;

    public Restaurant(String name) {
        this.name = name;
        this.menu = new ArrayList<>();
        this.specialOffers = new ArrayList<>();
        this.inventory = new Inventory();
    }


    // Getters and Setters
    public String getName() {
        return name;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    // Method to add item to menu
    public void addToMenu(MenuItem menuItem, int initialStock) {
        menu.add(menuItem);
        inventory.updateInventory(menuItem, initialStock);
    }

    // Method to remove item from the menu
    public void removeMenuItem(String itemName) {
        menu.removeIf(item -> item.getName().equalsIgnoreCase(itemName));
    }

    // Method to add special offer to menu
    public void addSpecialOffer(SpecialOffer specialOffer) {
        specialOffers.add(specialOffer);
    }

    // Method to process an order
    public void processOrder(User user, Cart cart) {
        Order order = new Order(user, this, cart.getItems(), specialOffers);

        if (canFulfillOrder(order)) {
            for (CartItem item : order.getItems()) {
                updateInventory(item.getMenuItem(), -item.getQuantity());
            }
            order.confirmOrder();
            System.out.println("Order confirmed! Total cost: $" + String.format("%.2f", order.getTotalCost()));
        } else {
            System.out.println("Unable to process the order due to insufficient stock.");
        }
    }

    // Method to check if the order can be fulfilled based on inventory
    private boolean canFulfillOrder(Order order) {
        for (CartItem item : order.getItems()) {
            if (!isItemAvailable(item.getMenuItem(), item.getQuantity())) {
                return false;
            }
        }
        return true;
    }

    public boolean isItemAvailable(MenuItem item, int quantity) {
        return inventory.checkAvailability(item) >= quantity;
    }


    // Method to update the menu
    public void updateInventory(MenuItem item, int quantity) {
        inventory.updateInventory(item, quantity);
    }

    public void updateMenuItemPrice(String itemName, double newPrice) {
        for (MenuItem item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.setPrice(newPrice);
                break;
            }
        }
    }
}

abstract class MenuItem {
    private String name;
    private double price;
    private String description;

    public MenuItem(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }
}

class FoodItem extends MenuItem {
    private final String cuisineType;

    public FoodItem(String name, double price, String description, String cuisineType) {
        super(name, price, description);
        this.cuisineType = cuisineType;
    }
}

class DrinkItem extends MenuItem {
    private final String beverageType;

    public DrinkItem(String name, double price, String description, String beverageType) {
        super(name, price, description);
        this.beverageType = beverageType;
    }
}


class Inventory {
    private Map<MenuItem, Integer> stock;

    public Inventory() {
        this.stock = new HashMap<>();
    }

    // Method to update the inventory
    public void updateInventory(MenuItem item, int quantity) {
        int currentStock = stock.getOrDefault(item, 0);
        int newStock = currentStock + quantity;
        if (newStock < 0) {
            newStock = 0; // Prevent negative stock
        }
        stock.put(item, newStock);
    }

    // Method to check the item availability
    public int checkAvailability(MenuItem item) {
        // Return the quantity available for the item
        return stock.getOrDefault(item, 0);
    }

    public void checkInventoryLevels() {
        for (Map.Entry<MenuItem, Integer> entry : stock.entrySet()) {
            if (entry.getValue() <= 5) { // Threshold for low stock
                System.out.println("Alert: Low stock for " + entry.getKey().getName());
            }
        }
    }
}

class SpecialOffer {
    private String offerDescription;
    private double discount;

    public SpecialOffer(String offerDescription, double discount) {
        this.offerDescription = offerDescription;
        this.discount = discount;
    }

    public double applyOffer(double cost) {
        return cost - (cost * (discount / 100.0));
    }
}

