package org.haiilo.checkoutkata.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String itemName) {
        super("Item not found: " + itemName);
    }
}
