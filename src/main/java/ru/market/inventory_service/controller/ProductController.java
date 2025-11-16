package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public CompletableFuture<ResponseEntity<List<ProductDto>>> getProductsByQuery(@RequestParam(defaultValue = "") String q) {
        if (q.isEmpty())
            return productService.getAll().thenApply(ResponseEntity::ok);
        else
            return productService.getProductsByQuery(q).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductDto>> getProductById(@PathVariable Integer id) {
        return productService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ProductDto>> addProduct(@RequestBody ProductDto product) {
        return productService.add(product).thenApply(
                result -> ResponseEntity.status(HttpStatus.CREATED).body(result)
        );
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductDto>> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductDto product
    ) { return productService.updateById(id, product).thenApply(ResponseEntity::ok); }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteProductById(@PathVariable Integer id) {
        return productService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
