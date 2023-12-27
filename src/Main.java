import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
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