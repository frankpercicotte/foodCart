package com.foodcart.ecommerce.adapters.inbound.http.product.mapper

import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateCategoryRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateCategoryResponse
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.usecase.product.CreateCategoryUseCase


fun CreateCategoryRequest.toInput(): CreateCategoryUseCase.Input =
    CreateCategoryUseCase.Input(
        name = this.name,
        profitMargin = this.profitMargin,
        maxDiscount =  this.maxDiscount,
        isActive = true,
    )


fun Category.toCreateResponse(): CreateCategoryResponse =
    CreateCategoryResponse(
        categoryId = this.categoryId,
        name = this.name,
        profitMargin = this.profitMargin,
        maxDiscount = this.maxDiscount,
        isActive = this.isActive,

    )
