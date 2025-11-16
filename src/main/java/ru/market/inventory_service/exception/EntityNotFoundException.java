package ru.market.inventory_service.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String className, Integer id) {
        super(
                String.format("%s with id = %s was not found.", className.toUpperCase(), id.toString())
        );
    }
}
