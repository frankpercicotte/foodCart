package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import com.foodcart.ecommerce.core.domain.product.service.CategoryPricingService
import com.foodcart.ecommerce.core.shared.Result
import java.math.BigDecimal
import kotlin.fold
import kotlin.getOrElse


class CreateProductUseCaseImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryPricingService: CategoryPricingService

): CreateProductUseCase {

    override fun execute(input: CreateProductUseCase.Input): Result<Product, ProductError> {
        val normalizedName = input.name.lowercase()

        if(productRepository.existsByNormalizedNameAndCategoryId(normalizedName, input.categoryId)){
            return  Result.Failure(ProductError.ProductNameAlreadyExists(input.name))
        }

        //Calculation price by cost and category
        val category = categoryRepository.findById(input.categoryId.toLong())

        if(category == null){
            return Result.Failure(ProductError.CategoryNotFound(input.categoryId.toLong()))
        }

        if(!category.isActive){
            return Result.Failure(ProductError.InactiveCategory(category.name))
        }

        val finalPrice = categoryPricingService.calculateFinalPriceOne(category, input.cost).getOrNull()

        if(finalPrice == null){
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
        return Result.Success(saveProduct)
    }


}