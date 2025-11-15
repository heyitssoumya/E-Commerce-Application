package dev.soumya.OrderService.dto;

public class OrderRequestDTO {
    private int productId;
    private int quantity;
    //private String userId;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
