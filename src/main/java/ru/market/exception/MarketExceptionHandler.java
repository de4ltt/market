package ru.market.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.market.inventory_service.exception.EntitiesRetrieveException;
import ru.market.inventory_service.exception.EntityNotFoundException;
import ru.market.inventory_service.exception.EntityRetrieveException;

@ControllerAdvice
public class MarketExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MarketException> handleProductNotFound(EntityNotFoundException e) {
        MarketException body = new MarketException(e.getMessage());
        e.printStackTrace(System.out);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MarketException> handleFailedToRetrieveProducts(RuntimeException e) {
        MarketException body = new MarketException(e.getMessage());
        e.printStackTrace(System.out);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<MarketException> handleThrowable(Throwable e) {
        MarketException body = new MarketException(e.getMessage());
        e.printStackTrace(System.out);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}

