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
import java.util.Random;

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

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByQuery(String query) {
        try {
            return productRepository.findByName(query).stream().map(productMapper::toDto).toList();
        } catch (Exception e) {
            throw new EntitiesRetrieveException(Product.class.getSimpleName());
        }
    }

    @Override
    @Transactional
    public ProductDto add(ProductDto dto) {
        if (dto.getBarcode() == null || dto.getBarcode().isEmpty()) {
            dto.setBarcode(generateEan13Barcode());
        }
        return super.add(dto);
    }

    @Override
    @Transactional
    public ProductDto updateById(Integer id, ProductDto dto) {
        if (dto.getBarcode() == null || dto.getBarcode().isEmpty()) {
            dto.setBarcode(generateEan13Barcode());
        }
        return super.updateById(id, dto);
    }

    private String generateEan13Barcode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            code.append(random.nextInt(10));
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = code.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checksum = (10 - (sum % 10)) % 10;
        code.append(checksum);
        return code.toString();
    }
}