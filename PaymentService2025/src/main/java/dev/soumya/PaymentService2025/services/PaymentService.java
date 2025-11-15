package dev.soumya.PaymentService2025.services;

import com.stripe.exception.StripeException;
import dev.soumya.PaymentService2025.paymentGateways.PaymentGateway;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private PaymentGateway paymentGateway;

    public PaymentService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String initiatePayment(Long orderId) throws StripeException {
        //fetch order details from orderService using orderId.
        //Make a call to payment gateway (razorpay, stripe) to generate payment link
        return paymentGateway.initiatePayment(orderId);
    }
}
