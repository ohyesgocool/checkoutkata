package org.haiilo.checkoutkata.controller;

import jakarta.validation.Valid;
import org.haiilo.checkoutkata.dto.CheckoutRequest;
import org.haiilo.checkoutkata.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("v1/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("/total")
    public ResponseEntity<Integer> calculateTotal(@Valid @RequestBody CheckoutRequest request) {
        int total = checkoutService.calculateTotal(request.getItems());
        return ResponseEntity.ok(total);
    }
}
