package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.core.service.InventoryCRUDService;
import ru.market.inventory_service.exception.EntitiesRetrieveException;
import ru.market.inventory_service.mapper.ProductMapper;
import ru.market.inventory_service.model.dto.ProductDto;
import ru.market.inventory_service.model.entity.Product;
import ru.market.inventory_service.repository.ProductRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService extends InventoryCRUDService<Product, ProductDto> {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductMapper productMapper, ProductRepository productRepository) {
        super(productRepository, productMapper);
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    /**
     * In future will be remade into elasticsearch
     */
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<ProductDto>> getProductsByQuery(String query) {
        return CompletableFuture.completedFuture(
                productRepository.findAllByName(query).parallelStream().map(productMapper::toDto).toList()
        ).handle((result, throwable) -> {
            if (throwable != null)
                throw new EntitiesRetrieveException(Product.class.getSimpleName());
            else return result;
        });
    }
}
