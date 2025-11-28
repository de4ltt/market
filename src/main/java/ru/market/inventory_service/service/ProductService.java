package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.core.service.MarketInventoryCRUDService;
import ru.market.inventory_service.exception.EntitiesRetrieveException;
import ru.market.inventory_service.mapper.ProductMapper;
import ru.market.inventory_service.model.dto.ProductDto;
import ru.market.inventory_service.model.entity.Product;
import ru.market.inventory_service.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService extends MarketInventoryCRUDService<Product, ProductDto> {

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
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByQuery(String query) {
        try {
            return productRepository.findAllByName(query).parallelStream().map(productMapper::toDto).toList();
        } catch (Exception e) {
            throw new EntitiesRetrieveException(Product.class.getSimpleName());
        }
    }
}
