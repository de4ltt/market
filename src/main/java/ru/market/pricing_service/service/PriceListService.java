package ru.market.pricing_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.pricing_service.model.dto.PriceListDto;
import ru.market.pricing_service.model.entity.PriceList;
import ru.market.pricing_service.repository.PriceListRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceListService {

    private final PriceListRepository priceListRepository;

    @Autowired
    public PriceListService(PriceListRepository priceListRepository) {
        this.priceListRepository = priceListRepository;
    }

    public List<PriceListDto> getAll() {
        return priceListRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PriceListDto getById(Integer id) {
        PriceList priceList = priceListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PriceList with id " + id + " not found"));
        return toDto(priceList);
    }

    public PriceListDto add(PriceListDto dto) {
        PriceList priceList = new PriceList();
        priceList.setType(dto.getType());
        priceList.setEffectiveDate(dto.getEffectiveDate());
        priceList.setEndDate(dto.getEndDate());

        return toDto(priceListRepository.save(priceList));
    }

    public PriceListDto updateById(Integer id, PriceListDto dto) {
        PriceList existing = priceListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PriceList with id " + id + " not found"));

        existing.setType(dto.getType());
        existing.setEffectiveDate(dto.getEffectiveDate());
        existing.setEndDate(dto.getEndDate());

        return toDto(priceListRepository.save(existing));
    }

    public void deleteById(Integer id) {
        if (!priceListRepository.existsById(id)) {
            throw new EntityNotFoundException("PriceList with id " + id + " not found");
        }
        priceListRepository.deleteById(id);
    }

    private PriceListDto toDto(PriceList entity) {
        return new PriceListDto(
                entity.getPriceListId(),
                entity.getType(),
                entity.getEffectiveDate(),
                entity.getEndDate()
        );
    }
}