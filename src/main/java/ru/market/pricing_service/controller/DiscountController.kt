package ru.market.pricing_service.controller

import jakarta.validation.Valid
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
import ru.market.pricing_service.model.dto.DiscountDto
import ru.market.pricing_service.service.DiscountService
import java.net.URI

@Validated
@RestController
@RequestMapping("/discounts")
open class DiscountController(
    private val service: DiscountService
) {

    @GetMapping
    fun getAll(): List<DiscountDto> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<DiscountDto> =
        ResponseEntity.ok(service.getById(id))

    @PostMapping
    fun add(@Valid @RequestBody dto: DiscountDto): ResponseEntity<DiscountDto> {
        val created = service.add(dto)
        val location = URI.create("/discounts/${created.discountId}")
        return ResponseEntity.created(location).body(created)
    }

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: Int, @Valid @RequestBody dto: DiscountDto): ResponseEntity<DiscountDto> =
        ResponseEntity.ok(service.updateById(id, dto))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int) = service.deleteById(id)
}
