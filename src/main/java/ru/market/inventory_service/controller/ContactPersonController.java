package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.service.ContactPersonService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/contact-persons")
@AllArgsConstructor
public class ContactPersonController {

    private final ContactPersonService contactPersonService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ContactPersonDto>>> getAllCounterparties() {
        return contactPersonService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ContactPersonDto>> getContactPersonById(@PathVariable Integer id) {
        return contactPersonService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ContactPersonDto>> addContactPerson(@RequestBody ContactPersonDto contactPersonDto) {
        return contactPersonService.add(contactPersonDto).thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ContactPersonDto>> updateContactPersonById(@PathVariable Integer id, @RequestBody ContactPersonDto contactPersonDto) {
        return contactPersonService.updateById(id, contactPersonDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteContactPersonById(@PathVariable Integer id) {
        return contactPersonService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}