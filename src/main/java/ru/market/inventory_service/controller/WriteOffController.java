package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.WriteOffDto;
import ru.market.inventory_service.service.WriteOffService;

import java.util.List;

@RestController
@RequestMapping("/write-offs")
@AllArgsConstructor
public class WriteOffController {

    private final WriteOffService writeOffService;

    @GetMapping
    public ResponseEntity<List<WriteOffDto>> getAllCounterparties() {
        return ResponseEntity.ok(writeOffService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WriteOffDto> getWriteOffById(@PathVariable Integer id) {
        return ResponseEntity.ok(writeOffService.getById(id));
    }

    @PostMapping
    public ResponseEntity<WriteOffDto> addWriteOff(@RequestBody WriteOffDto writeOffDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(writeOffService.add(writeOffDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WriteOffDto> updateWriteOffById(@PathVariable Integer id, @RequestBody WriteOffDto writeOffDto) {
        return ResponseEntity.ok(writeOffService.updateById(id, writeOffDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWriteOffById(@PathVariable Integer id) {
        writeOffService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
