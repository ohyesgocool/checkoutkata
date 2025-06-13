package org.haiilo.checkoutkata.controller;

import org.haiilo.checkoutkata.dto.CheckoutRequest;
import org.haiilo.checkoutkata.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<?> calculateTotal(@RequestBody CheckoutRequest request) {
        System.out.println("Incoming items: " + request.getItems());
        try {
            int total = checkoutService.calculateTotal(request.getItems());
            System.out.println("Total: " + total);
            return ResponseEntity.ok(total);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid item: " + e.getMessage());
        }
    }
}