package org.haiilo.checkoutkata.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequest {

    @NotEmpty(message = "'items' must not be empty")
    private List<String> items;
}
