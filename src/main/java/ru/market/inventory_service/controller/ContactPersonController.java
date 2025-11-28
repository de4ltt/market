package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.service.ContactPersonService;

import java.util.List;

@RestController
@RequestMapping("/contact-persons")
@AllArgsConstructor
public class ContactPersonController {

    private final ContactPersonService contactPersonService;

    @GetMapping
    public ResponseEntity<List<ContactPersonDto>> getAllCounterparties() {
        return ResponseEntity.ok(contactPersonService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactPersonDto> getContactPersonById(@PathVariable Integer id) {
        return ResponseEntity.ok(contactPersonService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ContactPersonDto> addContactPerson(@RequestBody ContactPersonDto contactPersonDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactPersonService.add(contactPersonDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactPersonDto> updateContactPersonById(@PathVariable Integer id, @RequestBody ContactPersonDto contactPersonDto) {
        return ResponseEntity.ok(contactPersonService.updateById(id, contactPersonDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactPersonById(@PathVariable Integer id) {
        contactPersonService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}