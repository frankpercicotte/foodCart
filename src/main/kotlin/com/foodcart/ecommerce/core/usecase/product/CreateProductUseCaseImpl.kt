package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import com.foodcart.ecommerce.core.shared.Result


class CreateProductUseCaseImpl(
    private val productRepository: ProductRepository
): CreateProductUseCase {

    override fun execute(input: CreateProductUseCase.Input): Result<Product, ProductError> {
        val normalizedName = input.name.lowercase()

        if(productRepository.existsByNormalizedNameAndCategoryId(normalizedName, input.categoryId)){
            return  Result.Failure(ProductError.ProductNameAlreadyExists(input.name))
        }

        val product = Product(
            productId = null,
            name = input.name,
            normalizedName = normalizedName,
            description =  input.description,
            price = input.price,
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