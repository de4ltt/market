package ru.market.inventory_service.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.exception.ProductNotFoundException;
import ru.market.inventory_service.mapper.ProductMapper;
import ru.market.inventory_service.model.dto.ProductDto;
import ru.market.inventory_service.repository.ProductRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    @Async
    @Transactional
    public CompletableFuture<List<ProductDto>> getAllProducts() {
        return CompletableFuture.supplyAsync(() ->
                productRepository.findAll().parallelStream().map(productMapper::toDto).toList()
        );
    }

    @Async
    @Transactional
    public CompletableFuture<ProductDto> getProductById(Integer id) {
        return CompletableFuture.supplyAsync(() ->
                productRepository.findById(id).map(productMapper::toDto)
                        .orElseThrow(() -> new ProductNotFoundException(id))
        );
    }

    /**
     * In future will be remade into elasticsearch
     */
    @Async
    @Transactional
    public CompletableFuture<List<ProductDto>> getProductsByQuery(String query) {
        return CompletableFuture.supplyAsync(() ->
                productRepository.findAllByName(query).parallelStream().map(productMapper::toDto).toList()
        );
    }
}
