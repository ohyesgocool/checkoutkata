package org.haiilo.checkoutkata.controller;

import org.haiilo.checkoutkata.dto.CheckoutRequest;
import org.haiilo.checkoutkata.service.CheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/checkout")
public class CheckoutController {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/total")
    public ResponseEntity<?> calculateTotal(@RequestBody CheckoutRequest request) {
        try {
            int total = checkoutService.calculateTotal(request.getItems());
            return ResponseEntity.ok(total);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid item in request: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid item: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during total calculation", e);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }
}
