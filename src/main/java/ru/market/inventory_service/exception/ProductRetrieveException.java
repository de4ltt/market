package ru.market.inventory_service.exception;

public class ProductRetrieveException extends RuntimeException {
    public ProductRetrieveException(Integer id) {
        super(
                String.join("Error trying to get product with id = %s.", id.toString())
        );
    }
}
