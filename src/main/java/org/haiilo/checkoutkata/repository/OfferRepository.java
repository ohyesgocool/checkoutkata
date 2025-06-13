package org.haiilo.checkoutkata.repository;

import org.haiilo.checkoutkata.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    Optional<Offer> findByItemName(String itemName);
}