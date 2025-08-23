package com.foodcart.ecommerce.core.samples

import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCase
import java.math.BigDecimal

object CreateProductInputSample {
    fun createProductInput(): CreateProductUseCase.Input {
        return CreateProductUseCase.Input (
            productId = 9L,
            name = "Quadro céu noturno",
            description = "Quadro céu noturno tamanho 60x30 cm",
            cost = BigDecimal("7.00"),
            discount = BigDecimal("0.50"),
            categoryId = 1L,
            stockQuantity = 100,
            isActive = true,
            imageUrl = "https://fastly.picsum.photos/id/831/200/300.jpg?hmac=IC6dJVWWVnJ-extXtn0D9QDwKwbQ-tA_M6UD2T9zUbQ",
        )
    }
}
