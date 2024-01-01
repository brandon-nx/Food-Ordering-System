import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

// Part 11: Execution
public class FoodOrderingSystem {
    private SpecialOffer specialOffer;
    private Customer customer;
    private Cart cart;
    private Random random;
    private Scanner userInput;
    private Map<String, Customer> customerMap;
    private List<Restaurant> restaurants;

    public FoodOrderingSystem() {
        // Initialise everything
        userInput = new Scanner(System.in);
        random = new Random();
        cart = new Cart();
        restaurants = new ArrayList<>();
        customerMap = new HashMap<>();
        loadCustomers("customers.txt");

        // Add restaurants
        restaurants.add(new Restaurant("Yummy Restaurant"));
        restaurants.add(new Restaurant("Delicious Restaurant"));

        // Add items to the menu of the restaurants
        restaurants.get(0).addToMenu(new FoodItem("Cheese Burger", 9.90, "Delicious cheesy chicken burger with pickle inside", "American"), 10);
        restaurants.get(0).addToMenu(new FoodItem("Chicken Chop", 14.90, "Delicious chicken chop sides with wedges and salad", "Western"), 10);
        restaurants.get(0).addToMenu(new DrinkItem("Cola", 2.90, "Refreshing cola drink", "Soft Drink"), 20);

        restaurants.get(1).addToMenu(new FoodItem("Carbonara Pasta", 12.90, "Delicious creamy cheesy spaghetti with chicken slices", "Italian"), 10);
        restaurants.get(1).addToMenu(new DrinkItem("Sprite", 2.90, "Refreshing cola drink", "Soft Drink"), 20);
        restaurants.get(1).addToMenu(new DrinkItem("Ice Lemon Tea", 2.90, "Refreshing cola drink", "Soft Drink"), 20);

        // Add special offers
        specialOffer = new SpecialOffer("10% Off on All Soft Drink", 10.0);
        restaurants.get(1).addSpecialOffer(specialOffer);
    }

    // Method to save customers to a file
    public void saveCustomers(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Customer customer : customerMap.values()) {
                writer.write(customer.serialise() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load customers from a file
    public void loadCustomers(String filename) {
        File myObj = new File(filename);
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Customer customer = Customer.deserialise(data);
                customerMap.put(customer.getMemberId(), customer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void displayMenu() {
        System.out.println("Welcome to FoodieBran!");
        System.out.println("-----------------------------------");
        displaySpecialOffers();
        System.out.print("""
            1. Register as a New Member to get Exclusive Discount
            2. Place Food Order
            3. View Order History
            4. Admin Login
            5. Exit
            -----------------------------------
            Enter your choice:""");
    }

    private void displaySpecialOffers() {
        System.out.println("Current Special Offers:");
        boolean hasOffers = false;
        for (Restaurant restaurant : restaurants) {
            for (SpecialOffer offer : restaurant.getSpecialOffers()) {
                System.out.println("- " + restaurant.getName() + ": " + offer.getOfferDescription());
                hasOffers = true;
            }
        }
        if (!hasOffers) {
            System.out.println("No special offers available at the moment.");
        }
        System.out.println("-----------------------------------");
    }




    // 1. Register as a New Library Member
    private void registerMember(){
        System.out.print("Enter your name: ");
        String name = userInput.nextLine();

        System.out.print("Enter your contact number: ");
        String contactNumber = userInput.nextLine();

        System.out.print("Enter your delivery address: ");
        String deliveryAddress = userInput.nextLine();

        // Generate random but unique member ID
        String memberId = generateMemberId();

        // Check if a member with the entered name already exists
        if (isExistingMemberName(name)) {
            System.out.println("A member with the name '" + name + "' already exists. Please enter a different name.");
        } else {
            customer = new Customer(memberId, name, contactNumber, deliveryAddress);
            customerMap.put(memberId, customer);

            System.out.println("-----------------------------------");
            System.out.println(name + ", you are now registered as a library member with ID: " + memberId);
            System.out.println("-----------------------------------");
        }
    }

    private String generateMemberId() {
        String memberId;
        do {
            int randomId = random.nextInt(99998) + 1; // Generate a random id between 00000 and 99999
            memberId = String.format("%05d", randomId);
        } while (customerMap.containsKey(memberId)); // Check for uniqueness

        return memberId;
    }

    private boolean isExistingMemberName(String name) {
        return customerMap.values().stream().anyMatch(member -> member.getName().equalsIgnoreCase(name));
    }





    // 2. Place Food Order
    private void placeOrder() {
        System.out.print("Enter your member ID: ");
        String memberId = userInput.nextLine();
        System.out.println("-----------------------------------");
        customer = customerMap.get(memberId);

        if (!customerMap.containsKey(memberId)) {
            System.out.println("Invalid member ID. Please try again.");
            System.out.println("-----------------------------------");
        } else {
            // Prompt the user to choose restaurant and display its menu
            Restaurant selectedRestaurant = getRestaurantChoiceAndMenu();

            // User returned to the main page
            if (selectedRestaurant == null) {
                return;
            }

            // Create a new cart for the current customer
            cart = new Cart();

            // Prompt the user to choose which item to order
            boolean isOrdering = true;
            while (isOrdering) {
                System.out.println("Enter the name of the item you want to order (type 'done' to finish, 'view' to view cart):");
                String inputOrder = userInput.nextLine();

                switch (inputOrder.toLowerCase()) {
                    case "done":
                        isOrdering = false;
                        finaliseOrder(selectedRestaurant);
                        break;
                    case "view":
                        cart.displayCartContents();
                        break;
                    default:
                        handleMenuItemSelection(selectedRestaurant, cart, inputOrder);
                }
            }
        }
    }



    private Restaurant getRestaurantChoiceAndMenu() {
        System.out.println("Please choose a restaurant or exit:");
        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println((i + 1) + ". " + restaurants.get(i).getName());
        }
        System.out.println((restaurants.size() + 1) + ". Return to main page");
        System.out.println("-----------------------------------");

        int option;

        do {
            try {
                System.out.print("Enter your choice: ");
                option = userInput.nextInt();
                userInput.nextLine();
                System.out.println("-----------------------------------");

                if (option == restaurants.size() + 1) {
                    System.out.println("Returning to the main page...");
                    System.out.println("-----------------------------------");
                    return null;
                } else if (option > 0 && option <= restaurants.size()) {
                    Restaurant selectedRestaurant = restaurants.get(option - 1);

                    System.out.println("Welcome to " + selectedRestaurant.getName() + "!");
                    System.out.println("-----------------------------------");
                    System.out.println("Menu:");
                    for (MenuItem menuItem : selectedRestaurant.getMenu()) {
                        System.out.println(menuItem.getName() + " - RM" + menuItem.getPrice());
                    }
                    System.out.println("-----------------------------------");

                    return selectedRestaurant;
                } else {
                    System.out.println("Invalid choice, please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                userInput.nextLine();
            }
        } while (true);
    }

    private void handleMenuItemSelection(Restaurant restaurant, Cart cart, String itemName) {
        // Find the item in the list of menu of the restaurant
        MenuItem menuItem = findMenuItem(restaurant, itemName);

        if (menuItem != null) {
            System.out.print("Enter quantity: ");
            int quantity = userInput.nextInt();
            userInput.nextLine();

            if (quantity > 0) {
                if (restaurant.isItemAvailable(menuItem, quantity)) {
                    restaurant.updateInventory(menuItem, -quantity);
                    cart.addItem(new CartItem(menuItem, quantity));
                    System.out.println("-----------------------------------");
                    System.out.println("Added " + quantity + " x " + menuItem.getName() + " to your cart.");
                    System.out.println("-----------------------------------");
                } else {
                    System.out.println("-----------------------------------");
                    System.out.println("Item not available in the desired quantity.");
                    System.out.println("-----------------------------------");
                }
            } else {
                System.out.println("-----------------------------------");
                System.out.println("Invalid quantity. Please enter a positive number.");
                System.out.println("-----------------------------------");
            }
        } else {
            System.out.println("-----------------------------------");
            System.out.println("Item not available or not found.");
            System.out.println("-----------------------------------");
        }
    }

    private MenuItem findMenuItem(Restaurant restaurant, String itemName) {
        for (MenuItem menuItem : restaurant.getMenu()) {
            if (menuItem.getName().equalsIgnoreCase(itemName)) {
                return menuItem;
            }
        }
        return null;
    }

    private void displayOrderSummary(Cart cart) {
        if (cart.getItems().isEmpty()) {
            System.out.println("-----------------------------------");
            System.out.println("No items in the order.");
            System.out.println("-----------------------------------");
            return;
        }

        System.out.println("-----------------------------------");
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
        System.out.println("-----------------------------------");
    }

    private void finaliseOrder(Restaurant selectedRestaurant) {
        if (cart.getItems().isEmpty()) {
            System.out.println("-----------------------------------");
            System.out.println("Your cart is empty. No charges applied.");
            System.out.println("-----------------------------------");
        } else {
            displayOrderSummary(cart);
            Order newOrder = selectedRestaurant.processOrder(customer, cart);
            if (newOrder != null) {
                customer.getOrderHistory().add(newOrder);
            }
        }
    }






    // 3. View Order History
    private void orderHistory() {
        System.out.print("Enter your member ID: ");
        String memberId = userInput.nextLine();
        customer = customerMap.get(memberId);

        if (!customerMap.containsKey(memberId)) {
            System.out.println("-----------------------------------");
            System.out.println("Invalid member ID. Please try again.");
            System.out.println("-----------------------------------");
        } else {
            Customer customer = customerMap.get(memberId);
            customer.viewOrderHistory();
        }
    }





    // 4. Admin Login
    private void adminLogin() {
        System.out.print("Enter admin username: ");
        String username = userInput.nextLine();
        System.out.print("Enter admin password: ");
        String password = userInput.nextLine();

        // Validate admin credentials
        if (username.equals("bwkt1n22") && password.equals("12345")) {
            adminMenu();
        } else {
            System.out.println("-----------------------------------");
            System.out.println("Invalid admin credentials. Please try again.");
            System.out.println("-----------------------------------");
        }
    }

    private void adminMenu(){
        int adminOption;
        do {
            displayAdminMenu();
            adminOption = userInput.nextInt();
            userInput.nextLine();
            System.out.println("-----------------------------------");

            switch (adminOption) {
                case 1:
                    addData();
                    break;
                case 2:
                    deleteData();
                    break;
                case 3:
                    modifyData();
                    break;
                case 4:
                    viewData();
                    break;
                case 5:
                    restockItem();
                    break;
                case 6:
                    System.out.println("Exiting admin mode...");
                    System.out.println("-----------------------------------");
                    break;
                default:
                    System.out.println("Invalid option. Please try again (1-4).");
                    break;
            }
        } while (adminOption != 6);
    }

    private void displayAdminMenu(){
        System.out.print("""
                -----------------------------------
                Admin Menu:
                1. Add Restaurant/Menu
                2. Delete Restaurant/Menu
                3. Modify Restaurant/Menu
                4. View All Existing Restaurant and Its Menu
                5. Restock Menu Item
                6. Exit Admin Mode
                -----------------------------------
                Enter your choice:""");
    }

    // Admin: 1. Add Data
    private void addData() {
        System.out.print("Do you want to add a new Restaurant (R) or a new Menu Item (M)? (R/M): ");
        String choice = userInput.nextLine().toUpperCase();

        if (choice.equals("R")) {
            System.out.print("Enter the name of the new restaurant: ");
            String restaurantName = userInput.nextLine();
            Restaurant newRestaurant = new Restaurant(restaurantName);
            restaurants.add(newRestaurant);
            System.out.println("Restaurant added successfully.");
        } else if (choice.equals("M")) {
            displayAllRestaurants();
            System.out.print("Enter the number of the restaurant to add menu item: ");
            int restaurantIndex = userInput.nextInt() - 1;
            userInput.nextLine();

            if (restaurantIndex >= 0 && restaurantIndex < restaurants.size()) {
                Restaurant selectedRestaurant = restaurants.get(restaurantIndex);
                addMenuItem(selectedRestaurant);
            } else {
                System.out.println("Invalid restaurant selection.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void addMenuItem(Restaurant restaurant) {
        System.out.print("Enter item name: ");
        String itemName = userInput.nextLine();
        System.out.print("Enter item price: ");
        double itemPrice = userInput.nextDouble();
        userInput.nextLine();
        System.out.print("Enter item description: ");
        String itemDescription = userInput.nextLine();
        System.out.print("Is this a Food item (F) or a Drink item (D)? (F/D): ");
        String itemType = userInput.nextLine().toUpperCase();

        MenuItem menuItem = null;
        if (itemType.equals("F")) {
            System.out.print("Enter cuisine type: ");
            String cuisineType = userInput.nextLine();
            menuItem = new FoodItem(itemName, itemPrice, itemDescription, cuisineType);
        } else if (itemType.equals("D")) {
            System.out.print("Enter beverage type: ");
            String beverageType = userInput.nextLine();
            menuItem = new DrinkItem(itemName, itemPrice, itemDescription, beverageType);
        }

        System.out.print("Enter initial stock quantity for this item: ");
        int quantity = userInput.nextInt();
        userInput.nextLine();

        if (menuItem != null) {
            restaurant.addToMenu(menuItem, quantity); // Use the provided quantity
            System.out.println("Menu item added successfully with initial stock of " + quantity + ".");
        } else {
            System.out.println("Invalid menu item type.");
        }
    }

    private void displayAllRestaurants() {
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants available.");
            return;
        }

        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println((i + 1) + ". " + restaurants.get(i).getName());
        }
    }

    // Admin: 2. Delete Data
    private void deleteData() {
        System.out.print("Do you want to delete a Restaurant (R) or a Menu Item (M)? (R/M): ");
        String choice = userInput.nextLine().toUpperCase();

        if (choice.equals("R")) {
            displayAllRestaurants();
            System.out.print("Enter the number of the restaurant to delete: ");
            int restaurantIndex = userInput.nextInt() - 1;
            userInput.nextLine();

            if (restaurantIndex >= 0 && restaurantIndex < restaurants.size()) {
                restaurants.remove(restaurantIndex);
                System.out.println("Restaurant deleted successfully.");
            } else {
                System.out.println("Invalid restaurant selection.");
            }
        } else if (choice.equals("M")) {
            displayAllRestaurants();
            System.out.print("Enter the number of the restaurant to delete a menu item: ");
            int restaurantIndex = userInput.nextInt() - 1;
            userInput.nextLine();

            if (restaurantIndex >= 0 && restaurantIndex < restaurants.size()) {
                Restaurant selectedRestaurant = restaurants.get(restaurantIndex);
                deleteMenuItem(selectedRestaurant);
            } else {
                System.out.println("Invalid restaurant selection.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void deleteMenuItem(Restaurant restaurant) {
        System.out.print("Enter the name of the menu item to delete: ");
        String itemName = userInput.nextLine();
        restaurant.removeMenuItem(itemName);
        System.out.println("Menu item deleted successfully.");
    }

    // Admin 3. Modify Data
    private void modifyData() {
        System.out.print("Do you want to modify a Restaurant (R) or a Menu Item (M)? (R/M): ");
        String choice = userInput.nextLine().toUpperCase();
        System.out.println("-----------------------------------");

        if (choice.equals("R")) {
            modifyRestaurant();
        } else if (choice.equals("M")) {
            modifyMenuItem();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void modifyRestaurant() {
        displayAllRestaurants();
        System.out.println("-----------------------------------");
        System.out.print("Enter the restaurant's number to modify: ");
        int restaurantIndex = userInput.nextInt() - 1;
        userInput.nextLine();
        System.out.println("-----------------------------------");

        if (restaurantIndex >= 0 && restaurantIndex < restaurants.size()) {
            Restaurant selectedRestaurant = restaurants.get(restaurantIndex);
            System.out.print("Enter new name for the restaurant: ");
            String newName = userInput.nextLine();
            selectedRestaurant.setName(newName);
            System.out.println("-----------------------------------");
            System.out.println("Restaurant name updated successfully.");
        } else {
            System.out.println("Invalid restaurant selection.");
        }
    }

    private void modifyMenuItem() {
        displayAllRestaurants();
        System.out.println("-----------------------------------");
        System.out.print("Enter the restaurant's number to modify its menu: ");
        int restaurantIndex = userInput.nextInt() - 1;
        userInput.nextLine();
        System.out.println("-----------------------------------");

        if (restaurantIndex >= 0 && restaurantIndex < restaurants.size()) {
            Restaurant selectedRestaurant = restaurants.get(restaurantIndex);
            displayMenuItems(selectedRestaurant);

            System.out.print("Enter the name of the menu item to modify: ");
            String itemName = userInput.nextLine();
            MenuItem menuItem = findMenuItem(selectedRestaurant, itemName);
            System.out.println("-----------------------------------");

            if (menuItem != null) {
                System.out.println("Selected Item: " + menuItem.getName());
                System.out.println("1. Change Name");
                System.out.println("2. Change Price");
                System.out.println("3. Change Description");
                if (menuItem instanceof FoodItem) {
                    System.out.println("4. Change Cuisine Type");
                } else if (menuItem instanceof DrinkItem) {
                    System.out.println("4. Change Beverage Type");
                }
                System.out.println("-----------------------------------");
                System.out.print("Enter your choice: ");
                int choice = userInput.nextInt();
                userInput.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter new name: ");
                        menuItem.setName(userInput.nextLine());
                        System.out.println("-----------------------------------");
                        System.out.println("Menu item updated successfully.");
                        break;
                    case 2:
                        System.out.print("Enter new price: ");
                        menuItem.setPrice(userInput.nextDouble());
                        userInput.nextLine();
                        System.out.println("-----------------------------------");
                        System.out.println("Menu item updated successfully.");
                        break;
                    case 3:
                        System.out.print("Enter new description: ");
                        menuItem.setDescription(userInput.nextLine());
                        System.out.println("-----------------------------------");
                        System.out.println("Menu item updated successfully.");
                        break;
                    case 4:
                        if (menuItem instanceof FoodItem) {
                            System.out.print("Enter new cuisine type: ");
                            ((FoodItem) menuItem).setCuisineType(userInput.nextLine());
                            System.out.println("-----------------------------------");
                        } else if (menuItem instanceof DrinkItem) {
                            System.out.print("Enter new beverage type: ");
                            ((DrinkItem) menuItem).setBeverageType(userInput.nextLine());
                            System.out.println("-----------------------------------");
                            System.out.println("Menu item updated successfully.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            } else {
                System.out.println("Menu item not found.");
            }
        } else {
            System.out.println("Invalid restaurant selection.");
        }
    }

    private void displayMenuItems(Restaurant restaurant) {
        System.out.println("Menu Items in " + restaurant.getName() + ":");
        for (MenuItem menuItem : restaurant.getMenu()) {
            System.out.println("  " + menuItem.getName());
        }
        System.out.println("-----------------------------------");
    }

    // Admin 4. View Data
    private void viewData() {
        for (Restaurant restaurant : restaurants) {
            System.out.println("Restaurant: " + restaurant.getName());
            List<MenuItem> menu = restaurant.getMenu();
            if (menu.isEmpty()) {
                System.out.println("  No menu items available.");
            } else {
                for (MenuItem item : menu) {
                    int stock = restaurant.getInventory().checkAvailability(item);
                    System.out.println("  " + item.getName() + " - $" + item.getPrice() + " - Stock: " + stock);
                }
            }
            System.out.println();
        }
    }

    // Admin 5. Restock Item
    private void restockItem() {
        displayAllRestaurants();
        System.out.println("-----------------------------------");
        System.out.print("Enter restaurant's number to restock one of its menu item: ");
        int restaurantIndex = userInput.nextInt() - 1;
        userInput.nextLine();
        System.out.println("-----------------------------------");

        if (restaurantIndex >= 0 && restaurantIndex < restaurants.size()) {
            Restaurant selectedRestaurant = restaurants.get(restaurantIndex);
            displayMenuItems(selectedRestaurant);

            System.out.print("Enter the name of the menu item to restock: ");
            String itemName = userInput.nextLine();
            MenuItem menuItem = findMenuItem(selectedRestaurant, itemName);

            if (menuItem != null) {
                System.out.print("Enter the quantity to add to stock: ");
                int quantity = userInput.nextInt();
                userInput.nextLine();

                if (quantity > 0) {
                    selectedRestaurant.updateInventory(menuItem, quantity);
                    System.out.println("-----------------------------------");
                    System.out.println("Restocked " + quantity + " units of " + itemName + ".");
                } else {
                    System.out.println("-----------------------------------");
                    System.out.println("Invalid quantity. Please enter a positive number.");
                }
            } else {
                System.out.println("-----------------------------------");
                System.out.println("Menu item not found.");
            }
        } else {
            System.out.println("Invalid restaurant selection.");
        }
    }



    public void run() {
        try {
            int option = -1;
            do {
                displayMenu();
                try {
                    option = userInput.nextInt();
                    System.out.println("-----------------------------------");
                    userInput.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("-----------------------------------");
                    System.out.println("Invalid input. Please enter a number.");
                    System.out.println("-----------------------------------");
                    userInput.nextLine();
                    continue;
                }

                switch (option) {
                    case 1:
                        registerMember();
                        break;
                    case 2:
                        placeOrder();
                        break;
                    case 3:
                        orderHistory();
                        break;
                    case 4:
                        adminLogin();
                        break;
                    case 5:
                        System.out.println("Thank you for using FoodieBran. Goodbye !");
                        System.out.println("-----------------------------------");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again (1-5).");
                        System.out.println("-----------------------------------");
                        break;
                }
            } while (option != 5);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        saveCustomers("customers.txt");
    }


    public static void main(String[] args) {
        FoodOrderingSystem foodOrderingSystem = new FoodOrderingSystem();
        foodOrderingSystem.run();
    }
}