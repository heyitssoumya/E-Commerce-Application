package dev.soumya.OrderService.dto;

public class PaymentResponseDTO {
    private String paymentUrl;

    public PaymentResponseDTO(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
