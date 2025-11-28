package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.dto.CounterpartyDto;
import ru.market.inventory_service.service.CounterpartyService;

import java.util.List;

@RestController
@RequestMapping("/counterparties")
@AllArgsConstructor
public class CounterpartyController {

    private final CounterpartyService counterpartyService;

    @GetMapping
    public ResponseEntity<List<CounterpartyDto>> getAllCounterparties() {
        return ResponseEntity.ok(counterpartyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CounterpartyDto> getCounterpartyById(@PathVariable Integer id) {
        return ResponseEntity.ok(counterpartyService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CounterpartyDto> addCounterparty(@RequestBody CounterpartyDto counterpartyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(counterpartyService.add(counterpartyDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CounterpartyDto> updateCounterpartyById(@PathVariable Integer id, @RequestBody CounterpartyDto counterpartyDto) {
        return ResponseEntity.ok(counterpartyService.updateById(id, counterpartyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounterpartyById(@PathVariable Integer id) {
        counterpartyService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<List<ContactPersonDto>> getCounterpartyContactsById(@PathVariable Integer id) {
        return ResponseEntity.ok(counterpartyService.getCounterpartyContactsById(id));
    }
}
