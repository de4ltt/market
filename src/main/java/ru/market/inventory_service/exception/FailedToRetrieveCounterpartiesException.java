package ru.market.inventory_service.exception;

public class FailedToRetrieveCounterpartiesException extends RuntimeException {
    public FailedToRetrieveCounterpartiesException() {
        super("Could not retrieve counterparties correctly.");
    }
}
