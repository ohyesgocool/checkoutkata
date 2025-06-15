package org.haiilo.checkoutkata.service;

import org.haiilo.checkoutkata.exception.ItemNotFoundException;
import org.haiilo.checkoutkata.model.Item;
import org.haiilo.checkoutkata.model.Offer;
import org.haiilo.checkoutkata.repository.ItemRepository;
import org.haiilo.checkoutkata.repository.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OfferRepository offerRepository;

    public int calculateTotal(List<String> items) {
        if (items == null || items.isEmpty()) {
            logger.info("Empty item list provided, total is 0");
            return 0;
        }

        Map<String, Long> itemCounts = items.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));

        int total = itemCounts.entrySet().stream()
                .mapToInt(entry -> calculateItemTotal(entry.getKey(), entry.getValue().intValue()))
                .sum();

        logger.info("Calculated checkout total: {}", total);
        return total;
    }

    private int calculateItemTotal(String itemName, int quantity) {
        Item item = itemRepository.findByItemName(itemName)
                .orElseThrow(() -> new ItemNotFoundException(itemName));

        Optional<Offer> offerOpt = offerRepository.findByItemName(itemName);

        return offerOpt.map(offer -> applyOfferPricing(item, offer, quantity))
                .orElseGet(() -> quantity * item.getUnitPrice());
    }

    private int applyOfferPricing(Item item, Offer offer, int quantity) {
        if (quantity < offer.getQuantity()) {
            return quantity * item.getUnitPrice();
        }

        int offerGroups = quantity / offer.getQuantity();
        int remainder = quantity % offer.getQuantity();
        return offerGroups * offer.getOfferPrice() + remainder * item.getUnitPrice();
    }
}
