package com.foodcart.ecommerce.core.samples

import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCase
import java.math.BigDecimal

object CreateProductInputSample {
    fun createProductInput(): CreateProductUseCase.Input {
        return CreateProductUseCase.Input (
            productId = "ac5e3398-c593-428e-8d3d-9d78c76e8286",
            name = "Quadro céu noturno",
            description = "Quadro céu noturno tamanho 60x30 cm",
            price = BigDecimal("10.50"),
            cost = BigDecimal("7.00"),
            discount = BigDecimal("0.50"),
            categoryId = "1a39e9e6-1776-4845-bbb0-15e6e25dfa43",
            stockQuantity = 100,
            isActive = true,
            imageUrl = "https://fastly.picsum.photos/id/831/200/300.jpg?hmac=IC6dJVWWVnJ-extXtn0D9QDwKwbQ-tA_M6UD2T9zUbQ",
        )
    }
}
