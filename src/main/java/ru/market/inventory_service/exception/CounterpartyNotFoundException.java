package ru.market.inventory_service.exception;

import lombok.NonNull;

public class CounterpartyNotFoundException extends RuntimeException {
    public CounterpartyNotFoundException(@NonNull Integer id) {
        super(
                String.join("Counterparty with id = %s was not found.", id.toString())
        );
    }
}
