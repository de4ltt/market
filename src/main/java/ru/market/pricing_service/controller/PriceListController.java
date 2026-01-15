package ru.market.pricing_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.market.pricing_service.model.dto.PriceListDto;
import ru.market.pricing_service.service.PriceListService;
import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/price-lists")
public class PriceListController {

    private final PriceListService service;

    @Autowired
    public PriceListController(PriceListService service) {
        this.service = service;
    }

    @GetMapping
    public List<PriceListDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceListDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<PriceListDto> add(@Valid @RequestBody PriceListDto dto, UriComponentsBuilder ucb) {
        PriceListDto created = service.add(dto);
        URI location = ucb.path("/price-lists/{id}").buildAndExpand(created.getPriceListId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceListDto> updateById(@PathVariable Integer id, @Valid @RequestBody PriceListDto dto) {
        return ResponseEntity.ok(service.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        service.deleteById(id);
    }
}
