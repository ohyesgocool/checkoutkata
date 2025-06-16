package org.haiilo.checkoutkata.repository;

import org.haiilo.checkoutkata.model.Offer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OfferRepositoryTest {

    @Autowired
    private OfferRepository offerRepository;

    @Test
    @DisplayName("Should find offer by itemName")
    void shouldFindOfferByItemName() {
        // Given: an offer for item "apple" exists in test data

        // When: finding offer by item name "apple"
        Optional<Offer> offer = offerRepository.findByItemName("apple");

        // Then: offer should be present with expected properties
        assertThat(offer).isPresent();
        assertThat(offer.get().getItemName()).isEqualTo("apple");
        assertThat(offer.get().getQuantity()).isEqualTo(2);
        assertThat(offer.get().getOfferPrice()).isEqualTo(45);
    }

    @Test
    @DisplayName("Should not find offer with non-existing item name")
    void shouldNotFindOfferWithInvalidItemName() {
        // Given: no offer exists for "dragonfruit"

        // When: finding offer by item name "dragonfruit"
        Optional<Offer> offer = offerRepository.findByItemName("dragonfruit");

        // Then: offer should not be present
        assertThat(offer).isNotPresent();
    }
}
