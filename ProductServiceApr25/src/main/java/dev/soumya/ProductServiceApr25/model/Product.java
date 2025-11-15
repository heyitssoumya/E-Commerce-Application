package dev.soumya.ProductServiceApr25.model;

import jakarta.persistence.*;

@Entity
public class Product extends BaseModel {
    private double price;
    private double rating;
    private int quantity;

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
