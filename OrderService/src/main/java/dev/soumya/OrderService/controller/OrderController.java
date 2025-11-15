package dev.soumya.OrderService.controller;

import dev.soumya.OrderService.dto.OrderRequestDTO;
import dev.soumya.OrderService.dto.PaymentResponseDTO;
import dev.soumya.OrderService.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping("/place")
    public ResponseEntity<PaymentResponseDTO> placeOrder(@RequestBody OrderRequestDTO request) {
        return ResponseEntity.ok(orderService.placeOrder(request));
    }
}
