package ru.market.pricing_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.market.pricing_service.model.dto.CheckDto;
import ru.market.pricing_service.service.CheckService;
import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/checks")
public class CheckController {

    private final CheckService service;

    @Autowired
    public CheckController(CheckService service) {
        this.service = service;
    }

    @GetMapping
    public List<CheckDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<CheckDto> add(@Valid @RequestBody CheckDto dto, UriComponentsBuilder ucb) {
        CheckDto created = service.add(dto);
        URI location = ucb.path("/checks/{id}").buildAndExpand(created.getCheckId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckDto> updateById(@PathVariable Integer id, @Valid @RequestBody CheckDto dto) {
        return ResponseEntity.ok(service.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        service.deleteById(id);
    }
}