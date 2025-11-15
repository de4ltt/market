package ru.market.inventory_service.exception;

public class FailedToRetrieveCounterpartyContactsException extends RuntimeException {
    public FailedToRetrieveCounterpartyContactsException(Integer id) {
        super(
                String.join("Could not retrieve counterparty's (id = %s) contact correctly.", id.toString())
        );
    }
}
