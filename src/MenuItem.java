public abstract class MenuItem {
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
    public void setName(String name) {
        this.name = name;
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
    public void setDescription(String description) {
        this.description = description;
    }
}

class FoodItem extends MenuItem {
    private String cuisineType;

    // Constructor
    public FoodItem(String name, double price, String description, String cuisineType) {
        super(name, price, description);
        this.cuisineType = cuisineType;
    }

    // Getters and Setters
    public String getCuisineType() {
        return cuisineType;
    }
    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }
}

class DrinkItem extends MenuItem {
    private String beverageType;

    // Constructor
    public DrinkItem(String name, double price, String description, String beverageType) {
        super(name, price, description);
        this.beverageType = beverageType;
    }

    // Getters and Setters
    public String getBeverageType() {
        return beverageType;
    }
    public void setBeverageType(String beverageType) {
        this.beverageType = beverageType;
    }
}