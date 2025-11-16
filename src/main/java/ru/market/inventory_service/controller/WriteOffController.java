package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.WriteOffDto;
import ru.market.inventory_service.service.WriteOffService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/write-offs")
@AllArgsConstructor
public class WriteOffController {

    private final WriteOffService writeOffService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<WriteOffDto>>> getAllCounterparties() {
        return writeOffService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<WriteOffDto>> getWriteOffById(@PathVariable Integer id) {
        return writeOffService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<WriteOffDto>> addWriteOff(@RequestBody WriteOffDto writeOffDto) {
        return writeOffService.add(writeOffDto).thenApply(
                result -> ResponseEntity.status(HttpStatus.CREATED).body(result)
        );
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<WriteOffDto>> updateWriteOffById(@PathVariable Integer id, @RequestBody WriteOffDto writeOffDto) {
        return writeOffService.updateById(id, writeOffDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteWriteOffById(@PathVariable Integer id) {
        return writeOffService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
