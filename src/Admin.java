import java.util.List;
import java.util.Scanner;

public class Admin {
    private List<Restaurant> restaurants;
    private Scanner userInput;

    public Admin(List<Restaurant> restaurants, Scanner userInput) {
        this.restaurants = restaurants;
        this.userInput = userInput;
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
            System.out.print("Enter the restaurant index to add menu item: ");
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
            System.out.print("Enter restaurant index to delete: ");
            if (!userInput.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                userInput.nextLine();
                return;
            }

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
            System.out.print("Enter restaurant index to delete a menu item: ");
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
        System.out.print("Enter the restaurant index to modify: ");
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
        System.out.print("Enter the restaurant index to modify its menu: ");
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

    private MenuItem findMenuItem(Restaurant restaurant, String itemName) {
        for (MenuItem menuItem : restaurant.getMenu()) {
            if (menuItem.getName().equalsIgnoreCase(itemName)) {
                return menuItem;
            }
        }
        return null;
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
        System.out.print("Enter restaurant's index to restock one of its menu item: ");
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

    public void showAdminMenu(){
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
}