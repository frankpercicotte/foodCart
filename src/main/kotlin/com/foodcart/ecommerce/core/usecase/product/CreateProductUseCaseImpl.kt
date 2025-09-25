package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.exception.CategoryNotFoundException
import com.foodcart.ecommerce.core.domain.common.exception.DatabaseOperationException
import com.foodcart.ecommerce.core.domain.common.exception.InactiveCategoryException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidPriceException
import com.foodcart.ecommerce.core.domain.common.exception.ProductNameAlreadyExistsException
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import com.foodcart.ecommerce.core.domain.product.service.CategoryPricingService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateProductUseCaseImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryPricingService: CategoryPricingService

): CreateProductUseCase {
    companion object {
        private val logger = LoggerFactory.getLogger(CreateProductUseCaseImpl::class.java)
    }

    override fun execute(input: CreateProductUseCase.Input): Product {
        val normalizedName = input.name.lowercase()

        if(productRepository.existsByNormalizedNameAndCategoryId(normalizedName, input.categoryId)){
            logger.error("Product name already exists name={} categoryId={}", input.name, input.categoryId)
            throw ProductNameAlreadyExistsException(input.name)
        }

        val category = categoryRepository.findById(input.categoryId)

        if(category == null){
            logger.error("Category not found id={}", input.categoryId)
            throw CategoryNotFoundException(input.categoryId)
        }

        if(!category.isActive){
            logger.error("Inactive category name={}", category.name)
            throw InactiveCategoryException(category.name)
        }

        val finalPrice = categoryPricingService.calculateFinalPriceOne(category, input.cost)

        val product = Product(
            productId = null,
            name = input.name,
            normalizedName = normalizedName,
            description =  input.description,
            price = finalPrice,
            cost = input.cost,
            discount = input.discount,
            categoryId = input.categoryId,
            stockQuantity = input.stockQuantity,
            isActive = input.isActive,
            imageUrl = input.imageUrl,
        )
        try {
            val saveProduct = productRepository.save(product)
            logger.info("Product created id={} name={} categoryId={}", saveProduct.productId, saveProduct.name, saveProduct.categoryId)
            return saveProduct
        } catch (ex: Exception) {
            logger.error("Database error while saving product: {}", ex.message, ex)
            throw DatabaseOperationException("save(product)", "Failed to save product", ex)
        }
    }


}