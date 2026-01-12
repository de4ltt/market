package ru.market.pricing_service.service

import org.springframework.stereotype.Service
import ru.market.inventory_service.repository.ProductRepository
import ru.market.pricing_service.model.dto.ProductPriceInCheckDto
import ru.market.pricing_service.model.entity.ProductPriceInCheck
import ru.market.pricing_service.repository.CheckRepository
import ru.market.pricing_service.repository.DiscountRepository
import ru.market.pricing_service.repository.PriceListRepository
import ru.market.pricing_service.repository.ProductPriceInCheckRepository

@Service
class ProductPriceInCheckService(
    private val productPriceInCheckRepository: ProductPriceInCheckRepository,
    private val productRepository: ProductRepository,
    private val checkRepository: CheckRepository,
    private val priceListRepository: PriceListRepository,
    private val discountRepository: DiscountRepository
) {

    fun getAll(): List<ProductPriceInCheckDto> =
        productPriceInCheckRepository.findAll().map { it.toDto() }

    fun getById(id: Int): ProductPriceInCheckDto {
        val entity = productPriceInCheckRepository.findById(id)
            .orElseThrow { NoSuchElementException("ProductPriceInCheck with id $id not found") }
        return entity.toDto()
    }

    fun add(dto: ProductPriceInCheckDto): ProductPriceInCheckDto {
        val product = productRepository.findById(dto.productId)
            .orElseThrow { NoSuchElementException("Product with id ${dto.productId} not found") }
        val check = checkRepository.findById(dto.checkId)
            .orElseThrow { NoSuchElementException("Check with id ${dto.checkId} not found") }
        val priceList = priceListRepository.findById(dto.priceListId)
            .orElseThrow { NoSuchElementException("PriceList with id ${dto.priceListId} not found") }
        val discount = dto.discountId.let {
            discountRepository.findById(it)
                .orElseThrow { NoSuchElementException("Discount with id $it not found") }
        }

        val entity = ProductPriceInCheck(
            productPriceInCheckId = 0,
            product = product,
            check = check,
            priceList = priceList,
            discount = discount,
            inputPrice = dto.inputPrice,
            finalPrice = dto.finalPrice,
            priceType = dto.priceType
        )
        return productPriceInCheckRepository.save(entity).toDto()
    }

    fun updateById(id: Int, dto: ProductPriceInCheckDto): ProductPriceInCheckDto {
        val existing = productPriceInCheckRepository.findById(id)
            .orElseThrow { NoSuchElementException("ProductPriceInCheck with id $id not found") }

        val product = productRepository.findById(dto.productId)
            .orElseThrow { NoSuchElementException("Product with id ${dto.productId} not found") }
        val check = checkRepository.findById(dto.checkId)
            .orElseThrow { NoSuchElementException("Check with id ${dto.checkId} not found") }
        val priceList = priceListRepository.findById(dto.priceListId)
            .orElseThrow { NoSuchElementException("PriceList with id ${dto.priceListId} not found") }
        val discount = dto.discountId.let {
            discountRepository.findById(it)
                .orElseThrow { NoSuchElementException("Discount with id $it not found") }
        }

        val updated = existing.copy(
            product = product,
            check = check,
            priceList = priceList,
            discount = discount,
            inputPrice = dto.inputPrice,
            finalPrice = dto.finalPrice,
            priceType = dto.priceType
        )
        return productPriceInCheckRepository.save(updated).toDto()
    }

    fun deleteById(id: Int) {
        if (!productPriceInCheckRepository.existsById(id)) {
            throw NoSuchElementException("ProductPriceInCheck with id $id not found")
        }
        productPriceInCheckRepository.deleteById(id)
    }

    private fun ProductPriceInCheck.toDto(): ProductPriceInCheckDto =
        ProductPriceInCheckDto(
            productPriceInCheckId = this.productPriceInCheckId,
            productId = this.product.productId,
            checkId = this.check.checkId,
            priceListId = this.priceList.priceListId,
            discountId = this.discount.discountId,
            inputPrice = this.inputPrice,
            finalPrice = this.finalPrice,
            priceType = this.priceType
        )
}