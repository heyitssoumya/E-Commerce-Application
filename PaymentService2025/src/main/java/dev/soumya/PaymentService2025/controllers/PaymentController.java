package dev.soumya.PaymentService2025.controllers;

import com.stripe.exception.StripeException;
import dev.soumya.PaymentService2025.services.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //To generate payment link or to initiate payment. This method will generate
    //payment link.
    @GetMapping("/initiate/{orderId}")
    public String initiatePayment(@PathVariable("orderId") Long orderId) throws StripeException {
        return paymentService.initiatePayment(orderId);
    }

}
