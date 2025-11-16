package ru.market.inventory_service.exception;

public class FailedToSaveEntityException extends RuntimeException {
    public FailedToSaveEntityException(String className) {
        super(
                String.format("Failed to add %s.", className.toLowerCase())
        );
    }
}
