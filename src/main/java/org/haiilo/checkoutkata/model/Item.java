package org.haiilo.checkoutkata.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Item {
    @Id
    private String name;
    private int unitPrice;
}