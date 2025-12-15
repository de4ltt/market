package ru.market.hr_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.market.hr_service.model.entity.Position;
import ru.market.hr_service.repository.PositionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Position getById(Integer id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }
}
