package ru.market.pricing_service.service

import org.springframework.stereotype.Service
import ru.market.pricing_service.model.dto.DiscountDto
import ru.market.pricing_service.model.entity.Discount
import ru.market.pricing_service.repository.DiscountRepository

@Service
class DiscountService(
    private val discountRepository: DiscountRepository
) {

    fun getAll(): List<DiscountDto> =
        discountRepository.findAll().map { it.toDto() }

    fun getById(id: Int): DiscountDto {
        val discount = discountRepository.findById(id)
            .orElseThrow { NoSuchElementException("Discount with id $id not found") }
        return discount.toDto()
    }

    fun add(dto: DiscountDto): DiscountDto {
        val discount = Discount(
            discountId = 0,
            discountType = dto.discountType,
            discountSize = dto.discountSize
        )
        return discountRepository.save(discount).toDto()
    }

    fun updateById(id: Int, dto: DiscountDto): DiscountDto {
        val existing = discountRepository.findById(id)
            .orElseThrow { NoSuchElementException("Discount with id $id not found") }

        val updated = existing.copy(
            discountType = dto.discountType,
            discountSize = dto.discountSize
        )
        return discountRepository.save(updated).toDto()
    }

    fun deleteById(id: Int) {
        if (!discountRepository.existsById(id)) {
            throw NoSuchElementException("Discount with id $id not found")
        }
        discountRepository.deleteById(id)
    }

    private fun Discount.toDto(): DiscountDto =
        DiscountDto(
            discountId = this.discountId,
            discountType = this.discountType,
            discountSize = this.discountSize
        )
}
