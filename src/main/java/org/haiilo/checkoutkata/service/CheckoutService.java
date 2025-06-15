package org.haiilo.checkoutkata.service;

import org.haiilo.checkoutkata.model.Item;
import org.haiilo.checkoutkata.model.Offer;
import org.haiilo.checkoutkata.repository.ItemRepository;
import org.haiilo.checkoutkata.repository.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;

    public CheckoutService(ItemRepository itemRepository, OfferRepository offerRepository) {
        this.itemRepository = itemRepository;
        this.offerRepository = offerRepository;
    }

    public int calculateTotal(List<String> items) {
        if (items == null || items.isEmpty()) {
            logger.info("No items provided, returning total: 0");
            return 0;
        }

        Map<String, Long> itemCounts = items.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

        logger.debug("Item counts: {}", itemCounts);

        int total = itemCounts.entrySet().stream()
                .mapToInt(entry -> {
                    int itemTotal = calculateItemTotal(entry.getKey(), entry.getValue().intValue());
                    logger.debug("Subtotal for {} (quantity: {}): {}", entry.getKey(), entry.getValue(), itemTotal);
                    return itemTotal;
                })
                .sum();

        logger.info("Final total: {}", total);
        return total;
    }

    private int calculateItemTotal(String itemName, int quantity) {
        String normalizedItemName = itemName.trim().toLowerCase();
        logger.debug("Looking up item with name: '{}'", normalizedItemName);

        Item item = itemRepository.findByItemName(normalizedItemName)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemName));

        Offer offer = offerRepository.findByItemName(normalizedItemName).orElse(null);

        if (offer != null && quantity >= offer.getQuantity()) {
            int offerGroups = quantity / offer.getQuantity();
            int remaining = quantity % offer.getQuantity();
            int total = offerGroups * offer.getOfferPrice() + remaining * item.getUnitPrice();

            logger.debug("Applied offer for {}: {} group(s) at {}, {} remaining at unit price {}",
                    normalizedItemName, offerGroups, offer.getOfferPrice(), remaining, item.getUnitPrice());
            return total;
        }

        int total = quantity * item.getUnitPrice();
        logger.debug("No offer for {}, using unit price: {}, total: {}", normalizedItemName, item.getUnitPrice(), total);
        return total;
    }
}
