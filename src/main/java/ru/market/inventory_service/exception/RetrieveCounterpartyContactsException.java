package ru.market.inventory_service.exception;

public class RetrieveCounterpartyContactsException extends RuntimeException {
    public RetrieveCounterpartyContactsException(Integer id) {
        super(
                String.join("Could not retrieve counterparty's (id = %s) contact correctly.", id.toString())
        );
    }
}
