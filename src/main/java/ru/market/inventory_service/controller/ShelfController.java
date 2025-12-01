package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ShelfDto;
import ru.market.inventory_service.service.ShelfService;

import java.util.List;

@RestController
@RequestMapping("/shelves")
@AllArgsConstructor
public class ShelfController {

    private final ShelfService shelfService;

    @GetMapping
    public ResponseEntity<List<ShelfDto>> getAllCounterparties() {
        return ResponseEntity.ok(shelfService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfDto> getShelfById(@PathVariable Integer id) {
        return ResponseEntity.ok(shelfService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ShelfDto> addShelf(@RequestBody ShelfDto shelfDto) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(shelfService.add(shelfDto));
    }

    @PostMapping("/in-refrigerator")
    public ResponseEntity<Void> putProductsInRefrigerator() {
        //TODO()
        return ResponseEntity.ok().build();
    }

    @PostMapping("/")
    public ResponseEntity<Void> putProductsOnRegularShelf() {
        //TODO()
        return ResponseEntity.ok().build();
    }

    @PostMapping("/")
    public ResponseEntity<Void> putProductsOnRegularShelf() {
        //TODO()
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelfDto> updateShelfById(@PathVariable Integer id, @RequestBody ShelfDto shelfDto) {
        return ResponseEntity.ok(shelfService.updateById(id, shelfDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelfById(@PathVariable Integer id) {
        shelfService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
