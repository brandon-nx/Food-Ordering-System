import java.time.LocalDate;
import java.util.*;

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
    public Order processOrder(Customer customer, Cart cart) {
        Order order = new Order(customer, this, cart.getItems(), specialOffers);

        if (canFulfillOrder(order)) {
            for (CartItem item : order.getItems()) {
                updateInventory(item.getMenuItem(), -item.getQuantity());
            }
            order.confirmOrder();
            System.out.println("Order confirmed! Total cost: $" + String.format("%.2f", order.getTotalCost()));
            return order; // Return the processed order
        } else {
            System.out.println("Unable to process the order due to insufficient stock.");
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





// Part 7: Customer Class
class Customer {
    private String memberId;
    private String name;
    private String contactNumber;
    private String deliveryAddress;
    private List<Order> orderHistory;

    // Constructor
    public Customer(String memberId, String name, String contactNumber, String deliveryAddress) {
        this.memberId = memberId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.deliveryAddress = deliveryAddress;
        this.orderHistory = new ArrayList<>();
    }

    // Getters and Setters
    public String getMemberId(){
        return memberId;
    }

    public void setMemberId(String memberId){
        this.memberId = memberId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber){
        this.contactNumber = contactNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    public void setDeliveryAddress(String deliveryAddress){
        this.deliveryAddress = deliveryAddress;
    }
    public List<Order> getOrderHistory () {
        return orderHistory;
    }
    public void setOrderHistory(String deliveryAddress){
        this.orderHistory = orderHistory;
    }

    // Method to view order history
    public void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("-----------------------------------");
            System.out.println("No past orders found.");
            System.out.println("-----------------------------------");
            return;
        }

        System.out.println("-----------------------------------");
        System.out.println("Order History:");
        for (Order order : orderHistory) {
            System.out.println("Order at " + order.getRestaurant().getName() + " - Total Cost: $" + String.format("%.2f", order.getTotalCost()));
        }
        System.out.println("-----------------------------------");
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
            System.out.println("-----------------------------------");
            System.out.println("Your cart is empty.");
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

    // Overloaded constructor without special offers
    public Order(Customer user, Restaurant restaurant, List<CartItem> items) {
        this(user, restaurant, items, new ArrayList<>()); // Calls the main constructor with an empty list of offers
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
        customerMap = new HashMap<>();
        cart = new Cart();
        restaurants = new ArrayList<>();

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

    private void displayMenu(){
        System.out.print("""
                Welcome to FoodieBran!
                -----------------------------------
                1. Register as a New Member to get Exclusive Discount
                2. Place Food Order
                3. View Order History
                4. Admin Login
                5. Exit
                -----------------------------------
                Enter your choice:""");
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
            Cart cart = new Cart();

            // Prompt the user to choose which item to order
            boolean isOrdering = true;
            while (isOrdering) {
                System.out.println("Enter the name of the item you want to order (type 'done' to finish, 'view' to view cart):");
                String inputOrder = userInput.nextLine();

                switch (inputOrder.toLowerCase()) {
                    case "done":
                        isOrdering = false;
                        break;
                    case "view":
                        cart.displayCartContents();
                        break;
                    default:
                        handleMenuItemSelection(selectedRestaurant, cart, inputOrder);
                }
            }

            if (!cart.getItems().isEmpty()) {
                displayOrderSummary(cart);
                Order newOrder = selectedRestaurant.processOrder(customer, cart); // Process the order and receive the Order object
                if (newOrder != null) {
                    customer.getOrderHistory().add(newOrder); // Add the new Order to the customer's order history
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

        int option = -1;  // Initialize option to an invalid value

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
                    viewData();
                    break;
                case 4:
                    System.out.println("Exiting admin mode...");
                    System.out.println("-----------------------------------");
                    break;
                default:
                    System.out.println("Invalid option. Please try again (1-4).");
                    break;
            }
        } while (adminOption != 4);
    }

    private void displayAdminMenu(){
        System.out.print("""
                -----------------------------------
                Admin Menu:
                1. Add Restaurant/Menu
                2. Delete Restaurant/Menu
                3. View All Existing Restaurant and Its Menu
                4. Exit Admin Mode
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

        if (menuItem != null) {
            restaurant.addToMenu(menuItem, 10); // Assuming default stock of 10
            System.out.println("Menu item added successfully.");
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

    // Admin 3. View Data
    private void viewData() {
        for (Restaurant restaurant : restaurants) {
            System.out.println("Restaurant: " + restaurant.getName());
            List<MenuItem> menu = restaurant.getMenu();
            if (menu.isEmpty()) {
                System.out.println("  No menu items available.");
            } else {
                for (MenuItem item : menu) {
                    System.out.println("  " + item.getName() + " - $" + item.getPrice());
                }
            }
            System.out.println();
        }
    }


    public void run() {
        int option;
        do {
            displayMenu();
            option = userInput.nextInt();
            userInput.nextLine();
            System.out.println("-----------------------------------");

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
                    break;
                default:
                    System.out.println("Invalid option. Please try again (1-5).");
                    break;
            }
        } while (option != 5);
    }

    public static void main(String[] args) {
        FoodOrderingSystem foodOrderingSystem = new FoodOrderingSystem();
        foodOrderingSystem.run();
    }
}