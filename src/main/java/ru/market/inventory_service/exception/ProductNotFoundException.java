package ru.market.inventory_service.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Integer id) {
        super(
                String.join("Product with id = %d was not found.", id.toString())
        );
    }
}
