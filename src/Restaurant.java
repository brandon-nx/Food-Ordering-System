import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String name;
    private List<MenuItem> menu;
    private List<SpecialOffer> specialOffers;
    private Inventory inventory;


    // Constructor
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
    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }
    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }

    public List<SpecialOffer> getSpecialOffers() {
        return specialOffers;
    }
    public void setSpecialOffers(List<SpecialOffer> specialOffers) {
        this.specialOffers = specialOffers;
    }

    public Inventory getInventory() {
        return inventory;
    }
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
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
    public Order processOrder(Customer customer, Cart cart) {
        Order order = new Order(customer, this, cart.getItems(), specialOffers);

        if (canFulfillOrder(order)) {
            try {
                // Deduct items from inventory
                for (CartItem item : order.getItems()) {
                    updateInventory(item.getMenuItem(), -item.getQuantity());
                }

                // Confirm order and display total cost
                order.confirmOrder();
                System.out.println("Order confirmed! Total cost: $" + String.format("%.2f", order.getTotalCost()));
                System.out.println("-----------------------------------");
                return order;

            } catch (IllegalArgumentException e) {
                System.out.println("Error processing order: " + e.getMessage());
                System.out.println("-----------------------------------");
                return null;
            }
        } else {
            System.out.println("Unable to process the order due to insufficient stock.");
            System.out.println("-----------------------------------");
            return null;
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

    // Method to check if the availability of the item
    public boolean isItemAvailable(MenuItem item, int quantity) {
        return inventory.checkAvailability(item) >= quantity;
    }


    // Method to update the inventory
    public void updateInventory(MenuItem item, int quantity) {
        inventory.updateInventory(item, quantity);
    }

    // Method to update menu item price
    public void updateMenuItemPrice(String itemName, double newPrice) {
        for (MenuItem item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                item.setPrice(newPrice);
                break;
            }
        }
    }
}