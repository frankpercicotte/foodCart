package com.foodcart.ecommerce.adapters.inbound.http.product.mapper

import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateProductRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateProductResponse
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCase


fun CreateProductRequest.toInput(): CreateProductUseCase.Input =
    CreateProductUseCase.Input(
        productId = null,
        name = this.name,
        description = this.description,
        cost = this.cost,
        discount = this.discount,
        categoryId = this.categoryId,
        stockQuantity = this.stockQuantity,
        isActive = true,
        imageUrl = this.imageUrl
    )


fun Product.toCreateResponse(): CreateProductResponse =
    CreateProductResponse(
        productId = this.productId,
        name = this.name,
        description = this.description,
        price = this.price,
        cost = this.cost,
        discount = this.discount,
        categoryId = this.categoryId,
        stockQuantity = this.stockQuantity,
        isActive = this.isActive,
        imageUrl = this.imageUrl
    )
