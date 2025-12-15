package ru.market.hr_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.hr_service.model.entity.Position;
import ru.market.hr_service.service.PositionService;

import java.util.List;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    // Справочник всех должностей / ставок
    @GetMapping
    public ResponseEntity<List<Position>> getAll() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    // Одна должность по id
    @GetMapping("/{id}")
    public ResponseEntity<Position> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(positionService.getById(id));
    }
}
