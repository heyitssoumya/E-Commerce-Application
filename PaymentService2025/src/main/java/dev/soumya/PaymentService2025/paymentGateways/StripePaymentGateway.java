package dev.soumya.PaymentService2025.paymentGateways;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class StripePaymentGateway implements PaymentGateway{
    //the value of apiKey comes from the environment variable
    @Value("${stripe.key}")
    private String apiKey;

    @Override
    public String initiatePayment(Long orderId) throws StripeException {
        Stripe.apiKey = apiKey;

        //creating Product object
        //code from https://docs.stripe.com/api/products/create
        ProductCreateParams productCreateParams =
                ProductCreateParams.builder().setName("Water Bottle").build();
        Product product = Product.create(productCreateParams);

        //1. Setting up the product catalogue
        //this code comes from https://docs.stripe.com/payment-links/api
        //55.62 -> 55.62 * 100 -> 5562L
        PriceCreateParams priceCreateParams =
                PriceCreateParams.builder()
                        .setCurrency("INR")
                        .setUnitAmount(5000L)   //why is this value taken in long an dnot in decimal
                        .setProduct(product.getId().toString())
                        .build();

        Price price = Price.create(priceCreateParams);

        //2. Creating payment link
        //this code comes from https://docs.stripe.com/payment-links/api
        PaymentLinkCreateParams paymentLinkCreateParams =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId().toString())
                                        .setQuantity(1L)
                                        .build()
                        )
                        .setAfterCompletion(
                                PaymentLinkCreateParams.AfterCompletion.builder()
                                        .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT) //Redirect is Callback
                                        .setRedirect(
                                                PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                        .setUrl("https://www.amazon.in/")
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        PaymentLink paymentLink = PaymentLink.create(paymentLinkCreateParams);

        //paymentLink is an object here
        return paymentLink.getUrl();
    }
}
