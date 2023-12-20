import java.util.*;

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




class Restaurant {
    private final String name;
    private final List<MenuItem> menu;
    private final List<SpecialOffer> specialOffers;
    private final Inventory inventory;

    public Restaurant(String name) {
        this.name = name;
        this.menu = new ArrayList<>();
        this.specialOffers = new ArrayList<>();
        this.inventory = new Inventory();
    }

    public String getName() {
        return name;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void addToMenu(MenuItem menuItem, int initialStock) {
        menu.add(menuItem);
        inventory.updateInventory(menuItem, initialStock);
    }

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

    public void updateInventory(MenuItem item, int quantity) {
        inventory.updateInventory(item, quantity);
    }

    public boolean isItemAvailable(MenuItem item, int quantity) {
        return inventory.checkAvailability(item) >= quantity;
    }
}





abstract class MenuItem {
    private final String name;
    private final double price;
    private final String description;

    public MenuItem(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
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

    // Additional methods specific to food items
}





class DrinkItem extends MenuItem {
    private final String beverageType;

    public DrinkItem(String name, double price, String description, String beverageType) {
        super(name, price, description);
        this.beverageType = beverageType;
    }

    // Additional methods specific to drink items
}





class SpecialOffer {
    private String offerDescription;
    private double discount; // This is a percentage (e.g., 10 for 10%)

    public SpecialOffer(String offerDescription, double discount) {
        this.offerDescription = offerDescription;
        this.discount = discount;
    }

    public double applyOffer(double cost) {
        return cost - (cost * (discount / 100.0));
    }
}





class Cart {
    private final List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(CartItem cartItem) {
        items.add(cartItem);
    }

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
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Cart Contents:");
        for (CartItem item : items) {
            System.out.println(item.getQuantity() + " x " + item.getMenuItem().getName() + " - $" + item.getMenuItem().getPrice());
        }
        System.out.println("Total Cost: $" + calculateTotalCost());
    }
}





class CartItem {
    private MenuItem menuItem;
    private int quantity;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
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
}





class User {
    private final String name;
    private final String contactInfo;
    private final String deliveryAddress;

    public User(String name, String contactInfo, String deliveryAddress) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.deliveryAddress = deliveryAddress;
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
        restaurant.processOrder(this, cart);
    }
}






public class Main {
    private static void displayMenu(Restaurant restaurant) {
        System.out.println("Welcome to " + restaurant.getName() + "!");
        System.out.println("Menu:");
        for (MenuItem menuItem : restaurant.getMenu()) {
            System.out.println(menuItem.getName() + " - $" + menuItem.getPrice());
        }
        System.out.println();
    }

    private static void takeUserOrder(Restaurant restaurant, User user, Cart cart) {
        Scanner scanner = new Scanner(System.in);
        boolean isOrdering = true;

        while (isOrdering) {
            System.out.println("Enter the name of the item you want to order (type 'done' to finish, 'view' to view cart):");
            String input = scanner.nextLine();

            switch (input.toLowerCase()) {
                case "done":
                    isOrdering = false;
                    break;
                case "view":
                    cart.displayCartContents();
                    break;
                default:
                    handleMenuItemSelection(restaurant, cart, scanner, input);
            }
        }
    }

    private static void handleMenuItemSelection(Restaurant restaurant, Cart cart, Scanner scanner, String itemName) {
        MenuItem menuItem = findMenuItem(restaurant, itemName);

        if (menuItem != null) {
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            if (restaurant.isItemAvailable(menuItem, quantity)) {
                cart.addItem(new CartItem(menuItem, quantity));
                System.out.println("Added " + quantity + " x " + menuItem.getName() + " to your cart.");
            } else {
                System.out.println("Item not available in the desired quantity.");
            }
        } else {
            System.out.println("Item not available or not found.");
        }
    }

    private static MenuItem findMenuItem(Restaurant restaurant, String itemName) {
        for (MenuItem menuItem : restaurant.getMenu()) {
            if (menuItem.getName().equalsIgnoreCase(itemName)) {
                return menuItem;
            }
        }
        return null;
    }

    private static void displayOrderSummary(Cart cart) {
        if (cart.getItems().isEmpty()) {
            System.out.println("No items in the order.");
            return;
        }

        System.out.println("Order Summary:");
        Map<String, Double> itemSummary = new HashMap<>();
        Map<String, Integer> itemCount = new HashMap<>();

        for (CartItem cartItem : cart.getItems()) {
            String itemName = cartItem.getMenuItem().getName();
            double itemTotalCost = cartItem.getMenuItem().getPrice() * cartItem.getQuantity();
            itemCount.put(itemName, itemCount.getOrDefault(itemName, 0) + cartItem.getQuantity());
            itemSummary.put(itemName, itemSummary.getOrDefault(itemName, 0.0) + itemTotalCost);
        }

        for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double totalCost = itemSummary.get(itemName);
            System.out.println(quantity + " x " + itemName + " - $" + String.format("%.2f", totalCost));
        }

        System.out.println("Total Cost: $" + String.format("%.2f", cart.calculateTotalCost()));
    }

    public static void main(String[] args) {
        // Create a sample restaurant with a menu
        Restaurant restaurant = new Restaurant("Sample Restaurant");
        restaurant.addToMenu(new FoodItem("Burger", 8.99, "Delicious burger with all the fixings", "American"), 10);
        restaurant.addToMenu(new DrinkItem("Cola", 2.49, "Refreshing cola drink", "Soda Drink"), 20);

        // Adding special offers
        SpecialOffer offer = new SpecialOffer("10% Off Everything", 10.0);
        restaurant.addSpecialOffer(offer);

        // Create a sample user and cart
        User user = new User("John Doe", "123-456-7890", "123 Main St");
        Cart cart = new Cart();

        // Display the menu to the user
        displayMenu(restaurant);

        // Prompt the user to place an order
        takeUserOrder(restaurant, user, cart);

        // Now process the order
        if (!cart.getItems().isEmpty()) {
            user.placeOrder(restaurant, cart);

            // Display the order summary
            displayOrderSummary(cart);
        } else {
            System.out.println("No items in the cart to process.");
        }
    }
}