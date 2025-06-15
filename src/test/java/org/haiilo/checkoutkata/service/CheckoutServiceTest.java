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

    private final Item apple = new Item(1L,"apple", 50);
    private final Item banana = new Item(2L,"banana", 30);

    private final Offer appleOffer = new Offer(1L,"apple", 3, 130);

    @BeforeEach
    void setUp() {
        // no manual setup needed due to @InjectMocks
    }

    @Test
    void calculateTotal_emptyList_returnsZero() {
        int total = checkoutService.calculateTotal(List.of());
        assertEquals(0, total);
    }

    @Test
    void calculateTotal_nullList_returnsZero() {
        int total = checkoutService.calculateTotal(null);
        assertEquals(0, total);
    }

    @Test
    void calculateTotal_singleItemWithoutOffer_returnsUnitPrice() {
        when(itemRepository.findByItemName("banana")).thenReturn(Optional.of(banana));
        when(offerRepository.findByItemName("banana")).thenReturn(Optional.empty());

        int total = checkoutService.calculateTotal(List.of("banana"));
        assertEquals(30, total);

        verify(itemRepository).findByItemName("banana");
        verify(offerRepository).findByItemName("banana");
    }

    @Test
    void calculateTotal_singleItemWithOfferBelowQuantity_returnsUnitPriceTimesQuantity() {
        when(itemRepository.findByItemName("apple")).thenReturn(Optional.of(apple));
        when(offerRepository.findByItemName("apple")).thenReturn(Optional.of(appleOffer));

        int total = checkoutService.calculateTotal(List.of("apple", "apple"));
        assertEquals(100, total);  // 2 * 50 = 100, offer needs 3 apples

        verify(itemRepository, times(1)).findByItemName("apple");
        verify(offerRepository, times(1)).findByItemName("apple");
    }

    @Test
    void calculateTotal_singleItemWithOfferApplied() {
        when(itemRepository.findByItemName("apple")).thenReturn(Optional.of(apple));
        when(offerRepository.findByItemName("apple")).thenReturn(Optional.of(appleOffer));

        int total = checkoutService.calculateTotal(List.of("apple", "apple", "apple"));
        assertEquals(130, total); // offer price for 3 apples

        total = checkoutService.calculateTotal(List.of("apple", "apple", "apple", "apple"));
        assertEquals(180, total); // 130 + 50

        verify(itemRepository, atLeastOnce()).findByItemName("apple");
        verify(offerRepository, atLeastOnce()).findByItemName("apple");
    }

    @Test
    void calculateTotal_multipleItemsWithMixedOffers() {
        when(itemRepository.findByItemName("apple")).thenReturn(Optional.of(apple));
        when(offerRepository.findByItemName("apple")).thenReturn(Optional.of(appleOffer));

        when(itemRepository.findByItemName("banana")).thenReturn(Optional.of(banana));
        when(offerRepository.findByItemName("banana")).thenReturn(Optional.empty());

        List<String> items = List.of("apple", "banana", "apple", "apple", "banana");

        int total = checkoutService.calculateTotal(items);
        // apples: 3 apples with offer 130, bananas: 2 * 30 = 60, total = 190
        assertEquals(190, total);
    }

    @Test
    void calculateTotal_itemNotFound_throwsException() {
        when(itemRepository.findByItemName("unknown")).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> checkoutService.calculateTotal(List.of("unknown")));
    }

    @Test
    void calculateTotal_emptyStringItem_throwsException() {
        when(itemRepository.findByItemName("")).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> checkoutService.calculateTotal(List.of("")));
    }

}
