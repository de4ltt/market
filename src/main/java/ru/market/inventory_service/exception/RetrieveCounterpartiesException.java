package ru.market.inventory_service.exception;

public class RetrieveCounterpartiesException extends RuntimeException {
    public RetrieveCounterpartiesException() {
        super("Could not retrieve counterparties correctly.");
    }
}
