package ru.market.exception;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class MarketException {

    private final String message;
    private final LocalDateTime timestamp;

    public MarketException(@NonNull String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
