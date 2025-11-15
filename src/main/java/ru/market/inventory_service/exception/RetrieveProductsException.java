package ru.market.inventory_service.exception;

public class RetrieveProductsException extends RuntimeException {
    public RetrieveProductsException() {
        super("Could not retrieve products correctly.");
    }
}
