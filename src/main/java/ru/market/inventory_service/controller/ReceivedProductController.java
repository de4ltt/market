package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ReceivedProductDto;
import ru.market.inventory_service.service.ReceivedProductService;

import java.util.List;

@RestController
@RequestMapping("/received-products")
@AllArgsConstructor
public class ReceivedProductController {

    private final ReceivedProductService receivedProductService;

    @GetMapping
    public ResponseEntity<List<ReceivedProductDto>> getAllCounterparties() {
        return ResponseEntity.ok(receivedProductService.getAll());
    }

    @GetMapping("/to-resolve")
    public ResponseEntity<List<ReceivedProductDto>> getArrivedProducts() {
        return ResponseEntity.ok(receivedProductService.getArrivedProducts());
    }

    @PostMapping("/to-resolve/reject")
    public ResponseEntity<Void> rejectProducts(@RequestBody List<ReceivedProductDto> products) {
        receivedProductService.refuseProducts(products);
        return ResponseEntity.ok().build();
    }

    @PostMapping("to-resolve/accept")
    public ResponseEntity<Void> acceptProducts(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Integer employeeId,
            @RequestBody List<ReceivedProductDto> products
    ) {
        receivedProductService.acceptProducts(employeeId, products);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceivedProductDto> getReceivedProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(receivedProductService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ReceivedProductDto> addReceivedProduct(@RequestBody ReceivedProductDto receivedProductDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receivedProductService.add(receivedProductDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceivedProductDto> updateReceivedProductById(@PathVariable Integer id, @RequestBody ReceivedProductDto receivedProductDto) {
        return ResponseEntity.ok(receivedProductService.updateById(id, receivedProductDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceivedProductById(@PathVariable Integer id) {
        receivedProductService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
