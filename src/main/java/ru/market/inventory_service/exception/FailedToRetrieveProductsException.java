package ru.market.inventory_service.exception;

public class FailedToRetrieveProductsException extends RuntimeException {
    public FailedToRetrieveProductsException() {
        super("Could not retrieve products correctly.");
    }
}
