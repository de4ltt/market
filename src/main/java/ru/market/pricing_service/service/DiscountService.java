package ru.market.pricing_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.pricing_service.model.dto.DiscountDto;
import ru.market.pricing_service.model.entity.Discount;
import ru.market.pricing_service.repository.DiscountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public List<DiscountDto> getAll() {
        return discountRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public DiscountDto getById(Integer id) {
        Discount discount = discountRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Discount with id " + id + " not found"));
        return toDto(discount);
    }

    public DiscountDto add(DiscountDto dto) {
        Discount discount = new Discount();
        discount.setDiscountType(dto.getDiscountType());
        discount.setDiscountSize(dto.getDiscountSize());

        return toDto(discountRepository.save(discount));
    }

    public DiscountDto updateById(Integer id, DiscountDto dto) {
        Discount existing = discountRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Discount with id " + id + " not found"));

        existing.setDiscountType(dto.getDiscountType());
        existing.setDiscountSize(dto.getDiscountSize());

        return toDto(discountRepository.save(existing));
    }

    public void deleteById(Integer id) {
        if (!discountRepository.existsById(id)) {
            throw new EntityNotFoundException("Discount with id " + id + " not found");
        }
        discountRepository.deleteById(id);
    }

    private DiscountDto toDto(Discount entity) {
        return new DiscountDto(
                entity.getDiscountId(),
        entity.getDiscountType(),
        entity.getDiscountSize()
        );
    }
}
