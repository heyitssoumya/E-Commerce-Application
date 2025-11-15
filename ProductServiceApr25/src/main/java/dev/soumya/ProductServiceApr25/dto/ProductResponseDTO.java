package dev.soumya.ProductServiceApr25.dto;

public class ProductResponseDTO {
    private String productName;
    private String productDescription;
    private double productPrice;
    private double rating;

    public ProductResponseDTO(String productName, String productDescription, double productPrice, double rating) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.rating = rating;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
