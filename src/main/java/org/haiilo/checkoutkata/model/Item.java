package org.haiilo.checkoutkata.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class Item {
    @Id
    private String name;
    @Column(name = "unit_price")
    private int unitPrice;
}