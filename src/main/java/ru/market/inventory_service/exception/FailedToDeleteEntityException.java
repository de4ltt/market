package ru.market.inventory_service.exception;

public class FailedToDeleteEntityException extends RuntimeException {
    public FailedToDeleteEntityException(String className, Integer id) {
        super(
                String.format("Failed to delete %s with id = %s.", className.toLowerCase(), id.toString())
        );
    }
}
