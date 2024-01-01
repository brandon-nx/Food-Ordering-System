public class SpecialOffer {
    private String offerDescription;
    private double discount;

    // Constructor
    public SpecialOffer(String offerDescription, double discount) {
        this.offerDescription = offerDescription;
        this.discount = discount;
    }

    // Getters and Setters
    public String getOfferDescription() {
        return offerDescription;
    }
    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }
    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    // Method to apply the offer
    public double applyOffer(double cost) {
        return cost - (cost * (discount / 100.0));
    }
}