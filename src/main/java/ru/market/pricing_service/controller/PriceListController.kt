package ru.market.pricing_service.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.market.pricing_service.model.dto.PriceListDto
import ru.market.pricing_service.service.PriceListService
import java.net.URI

@Validated
@RestController
@RequestMapping("/price-lists")
open class PriceListController(
    private val service: PriceListService
) {

    @GetMapping
    fun getAll(): List<PriceListDto> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<PriceListDto> =
        ResponseEntity.ok(service.getById(id))

    @PostMapping
    fun add(@Valid @RequestBody dto: PriceListDto): ResponseEntity<PriceListDto> {
        val created = service.add(dto)
        val location = URI.create("/price-lists/${created.priceListId}")
        return ResponseEntity.created(location).body(created)
    }

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: Int, @Valid @RequestBody dto: PriceListDto): ResponseEntity<PriceListDto> =
        ResponseEntity.ok(service.updateById(id, dto))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int) = service.deleteById(id)
}
