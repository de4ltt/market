package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.exception.FailedToRetrieveProductsException;
import ru.market.inventory_service.model.dto.ProductDto;
import ru.market.inventory_service.service.ProductService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ProductDto>>> getAllProducts() {
        return productService.getAllProducts().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductDto>> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id).thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ProductDto>>> getProductsByQuery(@RequestParam(defaultValue = "") String q) {
        return productService.getProductsByQuery(q).thenApply(ResponseEntity::ok);
    }
}
