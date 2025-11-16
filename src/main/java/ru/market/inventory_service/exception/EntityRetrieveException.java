package ru.market.inventory_service.exception;

public class EntityRetrieveException extends RuntimeException {
    public EntityRetrieveException(String className, Integer id) {
        super(
                String.format("Error trying to get %s with id = %s.", className.toLowerCase(), id.toString())
        );
    }
}
