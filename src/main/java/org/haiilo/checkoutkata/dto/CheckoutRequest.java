package org.haiilo.checkoutkata.dto;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequest {
    private List<String> items;
}
