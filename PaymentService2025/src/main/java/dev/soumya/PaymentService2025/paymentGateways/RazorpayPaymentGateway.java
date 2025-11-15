package dev.soumya.PaymentService2025.paymentGateways;

import org.springframework.stereotype.Component;

@Component
public class RazorpayPaymentGateway implements PaymentGateway{

    @Override
    public String initiatePayment(Long orderId) {
        //An http (network) call would be made to the Stripe payment gateway
        //In order to talk to Stripe, we need Stripe java dependency. Similarly,
        //razorpay we use razorpay java
        return null;
    }
}
