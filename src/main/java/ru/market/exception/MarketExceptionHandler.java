package ru.market.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.market.inventory_service.exception.RetrieveProductsException;
import ru.market.inventory_service.exception.ProductNotFoundException;

@ControllerAdvice
public class MarketExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RetrieveProductsException.class)
    public MarketException handleFailedToRetrieveProducts(RetrieveProductsException e) {
        return new MarketException(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public MarketException handleProductNotFound(ProductNotFoundException e) {
        return new MarketException(e.getMessage());
    }
}
