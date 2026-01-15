package ru.market.pricing_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.util.UriComponentsBuilder;
import ru.market.pricing_service.model.dto.ProductPriceInCheckDto;
import ru.market.pricing_service.service.ProductPriceInCheckService;
import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/product-price-in-checks")
public class ProductPriceInCheckController {

    private final ProductPriceInCheckService service;

    @Autowired
    public ProductPriceInCheckController(ProductPriceInCheckService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductPriceInCheckDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPriceInCheckDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductPriceInCheckDto> add(@Valid @RequestBody ProductPriceInCheckDto dto, UriComponentsBuilder ucb) {
        ProductPriceInCheckDto created = service.add(dto);
        URI location = ucb.path("/product-price-in-checks/{id}").buildAndExpand(created.getProductPriceInCheckId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductPriceInCheckDto> updateById(@PathVariable Integer id, @Valid @RequestBody ProductPriceInCheckDto dto) {
        return ResponseEntity.ok(service.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        service.deleteById(id);
    }
}
