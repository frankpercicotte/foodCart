package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import com.foodcart.ecommerce.core.domain.product.service.CategoryPricingService
import com.foodcart.ecommerce.core.shared.Result
import org.slf4j.LoggerFactory


class CreateProductUseCaseImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryPricingService: CategoryPricingService

): CreateProductUseCase {
    companion object {
        private val logger = LoggerFactory.getLogger(CreateProductUseCaseImpl::class.java)
    }

    override fun execute(input: CreateProductUseCase.Input): Result<Product, ProductError> {
        val normalizedName = input.name.lowercase()

        if(productRepository.existsByNormalizedNameAndCategoryId(normalizedName, input.categoryId)){
            logger.error("Product name already exists name={} categoryId={}", input.name, input.categoryId)
            return  Result.Failure(ProductError.ProductNameAlreadyExists(input.name))
        }

        val category = categoryRepository.findById(input.categoryId)

        if(category == null){
            logger.error("Category not found id={}", input.categoryId)
            return Result.Failure(ProductError.CategoryNotFound(input.categoryId))
        }

        if(!category.isActive){
            logger.error("Inactive category name={}", category.name)
            return Result.Failure(ProductError.InactiveCategory(category.name))
        }

        val finalPrice = categoryPricingService.calculateFinalPriceOne(category, input.cost).getOrNull()

        if(finalPrice == null){
            logger.error("Invalid final price calculated for cost={} categoryId={}", input.cost, input.categoryId)
            return Result.Failure(ProductError.InvalidPrice(input.cost))
        }

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
        val saveProduct = productRepository.save(product)
        logger.info("Product created id={} name={} categoryId={}", saveProduct.productId, saveProduct.name, saveProduct.categoryId)
        return Result.Success(saveProduct)
    }


}