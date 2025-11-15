package ru.market.inventory_service.exception;

public class RetrieveStorageLocationsException extends RuntimeException {
    public RetrieveStorageLocationsException() {
        super("Could not retrieve storage locations correctly.");
    }
}
