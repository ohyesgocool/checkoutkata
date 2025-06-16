package org.haiilo.checkoutkata.service;

import org.haiilo.checkoutkata.exception.ItemNotFoundException;
import org.haiilo.checkoutkata.model.Item;
import org.haiilo.checkoutkata.model.Offer;
import org.haiilo.checkoutkata.repository.ItemRepository;
import org.haiilo.checkoutkata.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    private final Item apple = new Item(1L, "apple", 50);
    private final Item banana = new Item(2L, "banana", 30);
    private final Offer appleOffer = new Offer(1L, "apple", 3, 130);

    @BeforeEach
    void setUp() {
        // No additional setup needed with @InjectMocks
    }

    @Test
    void calculateTotal_emptyList_returnsZero() {
        // Given: an empty item list
        // When: calculating the total
        int total = checkoutService.calculateTotal(List.of());

        // Then: total should be 0
        assertEquals(0, total);
    }

    @Test
    void calculateTotal_nullList_returnsZero() {
        // Given: a null list of items
        // When: calculating the total
        int total = checkoutService.calculateTotal(null);

        // Then: total should be 0
        assertEquals(0, total);
    }

    @Test
    void calculateTotal_singleItemWithoutOffer_returnsUnitPrice() {
        // Given: a single "banana" with no offer
        when(itemRepository.findByItemName("banana")).thenReturn(Optional.of(banana));
        when(offerRepository.findByItemName("banana")).thenReturn(Optional.empty());

        // When: calculating total
        int total = checkoutService.calculateTotal(List.of("banana"));

        // Then: total should equal unit price of banana
        assertEquals(30, total);
        verify(itemRepository).findByItemName("banana");
        verify(offerRepository).findByItemName("banana");
    }

    @Test
    void calculateTotal_singleItemWithOfferBelowQuantity_returnsUnitPriceTimesQuantity() {
        // Given: 2 apples with an offer requiring 3
        when(itemRepository.findByItemName("apple")).thenReturn(Optional.of(apple));
        when(offerRepository.findByItemName("apple")).thenReturn(Optional.of(appleOffer));

        // When: calculating total
        int total = checkoutService.calculateTotal(List.of("apple", "apple"));

        // Then: total should be 2 * 50 = 100 (no offer applied)
        assertEquals(100, total);
        verify(itemRepository, times(1)).findByItemName("apple");
        verify(offerRepository, times(1)).findByItemName("apple");
    }

    @Test
    void calculateTotal_singleItemWithOfferApplied() {
        // Given: 3 apples with an offer (3 for 130)
        when(itemRepository.findByItemName("apple")).thenReturn(Optional.of(apple));
        when(offerRepository.findByItemName("apple")).thenReturn(Optional.of(appleOffer));

        // When: calculating total with 3 apples
        int total = checkoutService.calculateTotal(List.of("apple", "apple", "apple"));

        // Then: total should be 130 (offer applied)
        assertEquals(130, total);

        // When: calculating total with 4 apples
        total = checkoutService.calculateTotal(List.of("apple", "apple", "apple", "apple"));

        // Then: offer applies to first 3, last one at unit price (130 + 50)
        assertEquals(180, total);

        verify(itemRepository, atLeastOnce()).findByItemName("apple");
        verify(offerRepository, atLeastOnce()).findByItemName("apple");
    }

    @Test
    void calculateTotal_multipleItemsWithMixedOffers() {
        // Given: apples with offer, bananas without offer
        when(itemRepository.findByItemName("apple")).thenReturn(Optional.of(apple));
        when(offerRepository.findByItemName("apple")).thenReturn(Optional.of(appleOffer));

        when(itemRepository.findByItemName("banana")).thenReturn(Optional.of(banana));
        when(offerRepository.findByItemName("banana")).thenReturn(Optional.empty());

        // When: calculating total for 3 apples and 2 bananas
        List<String> items = List.of("apple", "banana", "apple", "apple", "banana");
        int total = checkoutService.calculateTotal(items);

        // Then: total = 130 (apples) + 60 (bananas) = 190
        assertEquals(190, total);
    }

    @Test
    void calculateTotal_itemNotFound_throwsException() {
        // Given: an unknown item
        when(itemRepository.findByItemName("unknown")).thenReturn(Optional.empty());

        // When: calculating total
        // Then: ItemNotFoundException should be thrown
        assertThrows(ItemNotFoundException.class,
                () -> checkoutService.calculateTotal(List.of("unknown")));
    }

    @Test
    void calculateTotal_emptyStringItem_throwsException() {
        // Given: an item with an empty string name
        when(itemRepository.findByItemName("")).thenReturn(Optional.empty());

        // When: calculating total
        // Then: ItemNotFoundException should be thrown
        assertThrows(ItemNotFoundException.class,
                () -> checkoutService.calculateTotal(List.of("")));
    }
}
