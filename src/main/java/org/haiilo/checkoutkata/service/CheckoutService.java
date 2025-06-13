package org.haiilo.checkoutkata.service;

import org.haiilo.checkoutkata.model.Item;
import org.haiilo.checkoutkata.model.Offer;
import org.haiilo.checkoutkata.repository.ItemRepository;
import org.haiilo.checkoutkata.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;

    public CheckoutService(ItemRepository itemRepository, OfferRepository offerRepository) {
        this.itemRepository = itemRepository;
        this.offerRepository = offerRepository;
    }

    public int calculateTotal(List<String> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("No items provided, returning total: 0");
            return 0;
        }

        Map<String, Long> itemCounts = items.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        System.out.println("Item counts: " + itemCounts);

        int total = itemCounts.entrySet().stream()
                .mapToInt(entry -> {
                    int itemTotal = calculateItemTotal(entry.getKey(), entry.getValue().intValue());
                    System.out.println("Total for " + entry.getKey() + " (quantity: " + entry.getValue() + "): " + itemTotal);
                    return itemTotal;
                })
                .sum();
        System.out.println("Final total: " + total);
        return total;
    }

    private int calculateItemTotal(String itemName, int quantity) {
        String normalizedItemName = itemName.trim().toLowerCase();
        System.out.println("Looking up item with name: '" + normalizedItemName + "'");
        Item item = itemRepository.findById(normalizedItemName)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemName));
        Offer offer = offerRepository.findByItemName(normalizedItemName).orElse(null);

        if (offer != null && quantity >= offer.getQuantity()) {
            int offerGroups = quantity / offer.getQuantity();
            int remaining = quantity % offer.getQuantity();
            int total = offerGroups * offer.getOfferPrice() + remaining * item.getUnitPrice();
            System.out.println("Applied offer for " + normalizedItemName + ": " + offerGroups + " groups at " + offer.getOfferPrice() + ", " + remaining + " at unit price " + item.getUnitPrice());
            return total;
        }
        int total = quantity * item.getUnitPrice();
        System.out.println("No offer applied for " + normalizedItemName + ", using unit price: " + item.getUnitPrice());
        return total;
    }
}