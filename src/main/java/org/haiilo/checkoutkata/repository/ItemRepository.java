package org.haiilo.checkoutkata.repository;

import org.haiilo.checkoutkata.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    Optional<Item> findByItemName(String itemName);
}