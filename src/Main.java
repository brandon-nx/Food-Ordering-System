import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

// Part 1: Restaurant Class
class Restaurant {
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





// Part 2: MenuItem Class
abstract class MenuItem {
    private String name;
    private double price;
    private String description;

    // Constructor
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





// Part 3: FoodItem Function
class FoodItem extends MenuItem {
    private String cuisineType;

    // Constructor
    public FoodItem(String name, double price, String description, String cuisineType) {
        super(name, price, description);
        this.cuisineType = cuisineType;
    }
}






// Part 4: DrinkItem Function
class DrinkItem extends MenuItem {
    private String beverageType;

    // Constructor
    public DrinkItem(String name, double price, String description, String beverageType) {
        super(name, price, description);
        this.beverageType = beverageType;
    }
}





// Part 5: Inventory Class
class Inventory {
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
            newStock = 0; // Prevent negative stock
        }
        stock.put(item, newStock);
    }

    // Method to check the item availability
    public int checkAvailability(MenuItem item) {
        // Return the quantity available for the item
        return stock.getOrDefault(item, 0);
    }


    // Method to check inventory level
    public void checkInventoryLevels() {
        for (Map.Entry<MenuItem, Integer> entry : stock.entrySet()) {
            if (entry.getValue() <= 5) { // Threshold for low stock
                System.out.println("Alert: Low stock for " + entry.getKey().getName());
            }
        }
    }
}





// Part 6: SpecialOffer Class
class SpecialOffer {
    private String offerDescription;
    private double discount;

    // Constructor
    public SpecialOffer(String offerDescription, double discount) {
        this.offerDescription = offerDescription;
        this.discount = discount;
    }

    // Method to apply the offer
    public double applyOffer(double cost) {
        return cost - (cost * (discount / 100.0));
    }
}





// Part 7: User Class
class User {
    private String name;
    private String contactInfo;
    private String deliveryAddress;
    private List<Order> orderHistory;

    // Constructor
    public User(String name, String contactInfo, String deliveryAddress) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.deliveryAddress = deliveryAddress;
        this.orderHistory = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    // Method to place order
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





// Part 8: Cart Class
class Cart {
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
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Cart Contents:");
        for (CartItem item : items) {
            String formattedPrice = String.format("%.2f", item.getMenuItem().getPrice() * item.getQuantity());
            System.out.println(item.getQuantity() + " x " + item.getMenuItem().getName() + " - $" + formattedPrice);
        }
        System.out.println("Total Cost: $" + String.format("%.2f", calculateTotalCost()));
    }
}





// Part 9: CartItem Class
class CartItem {
    private MenuItem menuItem;
    private int quantity;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    // Getters and Setters
    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }
}





// Part 10: Order Class
class Order {
    private User user;
    private Restaurant restaurant;
    private List<CartItem> items;
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

    // Getter and Setters
    public User getUser() {
        return user;
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





// Part 11: Execution
public class FoodOrderingSystem {

    public void run() {
        System.out.println("Welcome to Ea-ting Restaurant!");

        int option;
        do {
            displayMenu();
            option = userInput.nextInt();
            userInput.nextLine();
            System.out.println("-----------------------------------");

            switch (option) {
                case 1:
                    //registerMember();
                    break;
                case 2:
                    //checkOutItem();
                    break;
                case 3:
                    //returnItem();
                    break;
                case 4:
                    //listAvailableItems();
                    break;
                case 5:
                    //listCheckedOutItems();
                    break;
                case 6:
                    System.out.println("Thank you for eating in Ea-ting Restaurant. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again (1-6).");
                    break;
            }
        } while (option != 6);
    }

    private void displayMenu(){
        System.out.print("""
                -----------------------------------
                1. Register as a New Customer
                2. Place Food Order
                3. Search Food by Title
                4. Order History
                5. Admin Login
                6. Exit
                -----------------------------------
                Enter your choice:""");
    }



    private static void displayMenu(Restaurant restaurant) {
        System.out.println("Welcome to " + restaurant.getName() + "!");
        System.out.println("Menu:");
        for (MenuItem menuItem : restaurant.getMenu()) {
            System.out.println(menuItem.getName() + " - RM" + menuItem.getPrice());
        }
        System.out.println();
    }

    private static void takeUserOrder(Restaurant restaurant, User user, Cart cart) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Select an option: \n1. Place Order \n2. View Order History \n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the remaining newline

            switch (choice) {
                case 1:
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
                    // Process the order if the cart is not empty
                    if (!cart.getItems().isEmpty()) {
                        user.placeOrder(restaurant, cart);
                        displayOrderSummary(cart);
                        cart = new Cart(); // Reset the cart for a new order
                    }
                    break;
                case 2:
                    user.viewOrderHistory();
                    break;
                case 3:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
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
            System.out.println(quantity + " x " + itemName + " - RM" + String.format("%.2f", totalCost));
        }

        System.out.println("Total Cost: RM" + String.format("%.2f", cart.calculateTotalCost()));
    }

    public static void main(String[] args) {
        // Create a sample restaurant with a menu
        Restaurant restaurant = new Restaurant("OrderEat Restaurant");
        restaurant.addToMenu(new FoodItem("Cheese Burger", 9.90, "Delicious cheesy chicken burger with pickle inside", "American"), 10);
        restaurant.addToMenu(new FoodItem("Chicken Chop", 14.90, "Delicious chicken chop sides with wedges and salad", "Western"), 10);
        restaurant.addToMenu(new FoodItem("Carbonara Pasta", 12.90, "Delicious creamy cheesy spaghetti with chicken slices", "Italian"), 10);
        restaurant.addToMenu(new DrinkItem("Cola", 2.90, "Refreshing cola drink", "Soft Drink"), 20);
        restaurant.addToMenu(new DrinkItem("Sprite", 2.90, "Refreshing cola drink", "Soft Drink"), 20);
        restaurant.addToMenu(new DrinkItem("Ice Lemon Tea", 2.90, "Refreshing cola drink", "Soft Drink"), 20);

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