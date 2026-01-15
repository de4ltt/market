package ru.market.pricing_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.model.entity.Product;
import ru.market.inventory_service.repository.ProductRepository;
import ru.market.pricing_service.model.dto.ProductPriceInCheckDto;
import ru.market.pricing_service.model.entity.Check;
import ru.market.pricing_service.model.entity.Discount;
import ru.market.pricing_service.model.entity.PriceList;
import ru.market.pricing_service.model.entity.ProductPriceInCheck;
import ru.market.pricing_service.repository.CheckRepository;
import ru.market.pricing_service.repository.DiscountRepository;
import ru.market.pricing_service.repository.PriceListRepository;
import ru.market.pricing_service.repository.ProductPriceInCheckRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductPriceInCheckService {

    private final ProductPriceInCheckRepository productPriceInCheckRepository;
    private final ProductRepository productRepository;
    private final CheckRepository checkRepository;
    private final PriceListRepository priceListRepository;
    private final DiscountRepository discountRepository;

    @Autowired
    public ProductPriceInCheckService(ProductPriceInCheckRepository productPriceInCheckRepository,
    ProductRepository productRepository,
    CheckRepository checkRepository,
    PriceListRepository priceListRepository,
    DiscountRepository discountRepository) {
        this.productPriceInCheckRepository = productPriceInCheckRepository;
        this.productRepository = productRepository;
        this.checkRepository = checkRepository;
        this.priceListRepository = priceListRepository;
        this.discountRepository = discountRepository;
    }

    public List<ProductPriceInCheckDto> getAll() {
        return productPriceInCheckRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public ProductPriceInCheckDto getById(Integer id) {
        ProductPriceInCheck entity = productPriceInCheckRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ProductPriceInCheck with id " + id + " not found"));
        return toDto(entity);
    }

    public ProductPriceInCheckDto add(ProductPriceInCheckDto dto) {
        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product with id " + dto.getProductId() + " not found"));
        Check check = checkRepository.findById(dto.getCheckId())
            .orElseThrow(() -> new EntityNotFoundException("Check with id " + dto.getCheckId() + " not found"));
        PriceList priceList = priceListRepository.findById(dto.getPriceListId())
            .orElseThrow(() -> new EntityNotFoundException("PriceList with id " + dto.getPriceListId() + " not found"));
        Discount discount = discountRepository.findById(dto.getDiscountId())
            .orElseThrow(() -> new EntityNotFoundException("Discount with id " + dto.getDiscountId() + " not found"));

        ProductPriceInCheck entity = new ProductPriceInCheck();
        entity.setProduct(product);
        entity.setCheck(check);
        entity.setPriceList(priceList);
        entity.setDiscount(discount);
        entity.setInputPrice(dto.getInputPrice());
        entity.setFinalPrice(dto.getFinalPrice());
        entity.setPriceType(dto.getPriceType());

        return toDto(productPriceInCheckRepository.save(entity));
    }

    public ProductPriceInCheckDto updateById(Integer id, ProductPriceInCheckDto dto) {
        ProductPriceInCheck existing = productPriceInCheckRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("ProductPriceInCheck with id " + id + " not found"));

        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product with id " + dto.getProductId() + " not found"));
        Check check = checkRepository.findById(dto.getCheckId())
            .orElseThrow(() -> new EntityNotFoundException("Check with id " + dto.getCheckId() + " not found"));
        PriceList priceList = priceListRepository.findById(dto.getPriceListId())
            .orElseThrow(() -> new EntityNotFoundException("PriceList with id " + dto.getPriceListId() + " not found"));
        Discount discount = discountRepository.findById(dto.getDiscountId())
            .orElseThrow(() -> new EntityNotFoundException("Discount with id " + dto.getDiscountId() + " not found"));

        existing.setProduct(product);
        existing.setCheck(check);
        existing.setPriceList(priceList);
        existing.setDiscount(discount);
        existing.setInputPrice(dto.getInputPrice());
        existing.setFinalPrice(dto.getFinalPrice());
        existing.setPriceType(dto.getPriceType());

        return toDto(productPriceInCheckRepository.save(existing));
    }

    public void deleteById(Integer id) {
        if (!productPriceInCheckRepository.existsById(id)) {
            throw new EntityNotFoundException("ProductPriceInCheck with id " + id + " not found");
        }
        productPriceInCheckRepository.deleteById(id);
    }

    private ProductPriceInCheckDto toDto(ProductPriceInCheck entity) {
        return new ProductPriceInCheckDto(
                entity.getProductPriceInCheckId(),
        entity.getProduct().getProductId(),
        entity.getCheck().getCheckId(),
        entity.getPriceList().getPriceListId(),
        entity.getDiscount().getDiscountId(),
        entity.getInputPrice(),
        entity.getFinalPrice(),
        entity.getPriceType()
        );
    }
}