package ru.market.pricing_service.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.market.pricing_service.model.dto.CheckDto
import ru.market.pricing_service.service.CheckService
import java.net.URI

@Validated
@RestController
@RequestMapping("/checks")
open class CheckController(
    private val service: CheckService
) {

    @GetMapping
    fun getAll(): List<CheckDto> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<CheckDto> =
        ResponseEntity.ok(service.getById(id))

    @PostMapping
    fun add(@Valid @RequestBody dto: CheckDto): ResponseEntity<CheckDto> {
        val created = service.add(dto)
        val location = URI.create("/checks/${created.checkId}")
        return ResponseEntity.created(location).body(created)
    }

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: Int, @Valid @RequestBody dto: CheckDto): ResponseEntity<CheckDto> =
        ResponseEntity.ok(service.updateById(id, dto))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int) = service.deleteById(id)
}
