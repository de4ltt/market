package ru.market.pricing_service.service

import org.springframework.stereotype.Service
import ru.market.pricing_service.model.dto.PriceListDto
import ru.market.pricing_service.model.entity.PriceList
import ru.market.pricing_service.repository.PriceListRepository

@Service
class PriceListService(
    private val priceListRepository: PriceListRepository
) {

    fun getAll(): List<PriceListDto> =
        priceListRepository.findAll().map { it.toDto() }

    fun getById(id: Int): PriceListDto {
        val priceList = priceListRepository.findById(id)
            .orElseThrow { NoSuchElementException("PriceList with id $id not found") }
        return priceList.toDto()
    }

    fun add(dto: PriceListDto): PriceListDto {
        val priceList = PriceList(
            priceListId = 0,
            type = dto.type,
            effectiveDate = dto.effectiveDate,
            endDate = dto.endDate
        )
        return priceListRepository.save(priceList).toDto()
    }

    fun updateById(id: Int, dto: PriceListDto): PriceListDto {
        val existing = priceListRepository.findById(id)
            .orElseThrow { NoSuchElementException("PriceList with id $id not found") }

        return priceListRepository.save(existing.copy(
            type = dto.type,
            effectiveDate = dto.effectiveDate,
            endDate = dto.endDate
        )).toDto()
    }

    fun deleteById(id: Int) {
        if (!priceListRepository.existsById(id)) {
            throw NoSuchElementException("PriceList with id $id not found")
        }
        priceListRepository.deleteById(id)
    }

    private fun PriceList.toDto(): PriceListDto =
        PriceListDto(
            priceListId = this.priceListId,
            type = this.type,
            effectiveDate = this.effectiveDate,
            endDate = this.endDate
        )
}
