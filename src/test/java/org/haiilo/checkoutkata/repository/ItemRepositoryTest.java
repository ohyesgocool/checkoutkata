package org.haiilo.checkoutkata.repository;

import org.haiilo.checkoutkata.model.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("Should find item by itemName")
    void shouldFindItemByItemName() {
        // Given: 'apple' is preloaded in the test database via data.sql or similar mechanism

        // When: finding item by name 'apple'
        Optional<Item> item = itemRepository.findByItemName("apple");

        // Then: item should be present with correct name and price
        assertThat(item).isPresent();
        assertThat(item.get().getItemName()).isEqualTo("apple");
        assertThat(item.get().getUnitPrice()).isEqualTo(30);
    }

    @Test
    @DisplayName("Should not find item with non-existing name")
    void shouldNotFindItemWithInvalidName() {
        // Given: no item named 'nonexistent' in the database

        // When: finding item by name 'nonexistent'
        Optional<Item> item = itemRepository.findByItemName("nonexistent");

        // Then: item should not be present
        assertThat(item).isNotPresent();
    }
}
