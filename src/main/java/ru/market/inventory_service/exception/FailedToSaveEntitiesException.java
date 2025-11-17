package ru.market.inventory_service.exception;

public class FailedToSaveEntitiesException extends RuntimeException
{
    public FailedToSaveEntitiesException(String className) {
        super(
                String.format("Failed to save %s list", className.toLowerCase())
        );
    }
}
