package org.haiilo.checkoutkata.controller;

import org.haiilo.checkoutkata.dto.CheckoutRequest;
import org.haiilo.checkoutkata.service.CheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/checkout")
public class CheckoutController {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("/total")
    public ResponseEntity<Integer> calculateTotal(@RequestBody CheckoutRequest request) {
        int total = checkoutService.calculateTotal(request.getItems());
        return ResponseEntity.ok(total);
    }
}
