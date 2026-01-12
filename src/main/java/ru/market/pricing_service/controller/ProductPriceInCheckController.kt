package ru.market.pricing_service.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import ru.market.pricing_service.model.dto.ProductPriceInCheckDto
import ru.market.pricing_service.service.ProductPriceInCheckService
import java.net.URI

@Validated
@RestController
@RequestMapping("/product-price-in-checks")
open class ProductPriceInCheckController(
    private val service: ProductPriceInCheckService
) {

    @GetMapping
    fun getAll(): List<ProductPriceInCheckDto> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<ProductPriceInCheckDto> =
        ResponseEntity.ok(service.getById(id))

    @PostMapping
    fun add(@Valid @RequestBody dto: ProductPriceInCheckDto): ResponseEntity<ProductPriceInCheckDto> {
        val created = service.add(dto)
        val location = URI.create("/product-price-in-checks/${created.productPriceInCheckId}")
        return ResponseEntity.created(location).body(created)
    }

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: Int, @Valid @RequestBody dto: ProductPriceInCheckDto): ResponseEntity<ProductPriceInCheckDto> =
        ResponseEntity.ok(service.updateById(id, dto))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int) = service.deleteById(id)
}
