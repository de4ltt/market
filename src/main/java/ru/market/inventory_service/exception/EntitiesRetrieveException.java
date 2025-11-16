package ru.market.inventory_service.exception;

public class EntitiesRetrieveException extends RuntimeException {
    public EntitiesRetrieveException(String className) {
        super(
                String.format("Could not retrieve %s correctly.", className.toLowerCase())
        );
    }
}
