package ru.market.exception;

import lombok.NonNull;

import java.time.LocalDateTime;

public class MarketException {

    public MarketException(@NonNull String message) {
        @NonNull LocalDateTime timestamp = LocalDateTime.now();
    }
}
