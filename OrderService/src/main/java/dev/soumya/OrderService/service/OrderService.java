package dev.soumya.OrderService.service;

import dev.soumya.OrderService.dto.OrderRequestDTO;
import dev.soumya.OrderService.dto.PaymentResponseDTO;
import dev.soumya.OrderService.dto.ProductResponseDTO;
import dev.soumya.OrderService.model.Order;
import dev.soumya.OrderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    public OrderService(RestTemplate restTemplate, OrderRepository orderRepository) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
    }

    public PaymentResponseDTO placeOrder(OrderRequestDTO request) {
        //Fetch product details
        String productUrl = productServiceUrl + "/product/" + request.getProductId();
        ProductResponseDTO product = restTemplate.getForObject(productUrl, ProductResponseDTO.class);

        if (product == null)
            throw new RuntimeException("Product not found!");

        //Calculate total
        double total = product.getPrice() * request.getQuantity();

        //Create Order
        Order order = new Order();
        order.setProductId(product.getId());
        order.setQuantity(request.getQuantity());
        order.setAmount(total);
        //order.setUserId(request.getUserId());
        order.setStatus("CREATED");
        orderRepository.save(order);

        //Call Payment Service
        String payUrl = paymentServiceUrl + "/payments/initiate/" + order.getId();
        String paymentUrl = restTemplate.getForObject(payUrl, String.class);

        //Update order with payment link
        order.setPaymentUrl(paymentUrl);
        order.setStatus("PAYMENT_LINK_CREATED");
        orderRepository.save(order);

        return new PaymentResponseDTO(paymentUrl);
    }
}
