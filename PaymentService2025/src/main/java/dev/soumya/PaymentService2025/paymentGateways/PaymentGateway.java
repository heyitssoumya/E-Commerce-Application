package dev.soumya.PaymentService2025.paymentGateways;

import com.stripe.exception.StripeException;

public interface PaymentGateway {
    String initiatePayment(Long orderId) throws StripeException;
}
