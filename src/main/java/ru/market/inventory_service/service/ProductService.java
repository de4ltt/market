package ru.market.inventory_service.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.exception.ProductNotFoundException;
import ru.market.inventory_service.exception.ProductRetrieveException;
import ru.market.inventory_service.exception.RetrieveProductsException;
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
        return CompletableFuture.completedFuture(
                productRepository.findAll().parallelStream().map(productMapper::toDto).toList()
        ).handle((result, throwable) -> {
            if (throwable != null)
                throw new RetrieveProductsException();
            else return result;
        });
    }

    @Async
    @Transactional
    public CompletableFuture<ProductDto> getProductById(Integer id) {
        return CompletableFuture.completedFuture(
                productRepository.findById(id).map(productMapper::toDto)
                        .orElseThrow(() -> new ProductNotFoundException(id))
        ).handle((result, throwable) -> {
            if (throwable != null)
                throw new ProductRetrieveException(id);
            else return result;
        });
    }

    /**
     * In future will be remade into elasticsearch
     */
    @Async
    @Transactional
    public CompletableFuture<List<ProductDto>> getProductsByQuery(String query) {
        return CompletableFuture.completedFuture(
                productRepository.findAllByName(query).parallelStream().map(productMapper::toDto).toList()
        ).handle((result, throwable) -> {
            if (throwable != null)
                throw new RetrieveProductsException();
            else return result;
        });
    }
}
