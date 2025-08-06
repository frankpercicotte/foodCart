package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import com.foodcart.ecommerce.core.shared.Result
import com.foodcart.ecommerce.core.samples.CreateProductInputSample
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import kotlin.test.assertTrue

class CreateProductUseCaseTest {
    @InjectMocks
    private lateinit var createProductUseCase: CreateProductUseCaseImpl

    @Mock
    private lateinit var productRepository: ProductRepository

    private fun <T> anyObject(type: Class<T>): T = any(type)

    @Test
    fun `should create a new product successfully`(){
        val input = CreateProductInputSample.createProductInput()


        val expectedProduct = Product(
            productId = null,
            name = input.name,
            description = input.description,
            price = input.price,
            cost = input.cost,
            discount = input.discount,
            categoryId = input.categoryId,
            stockQuantity = input.stockQuantity,
            isActive = input.isActive,
            imageUrl = input.imageUrl,
        )

        `when`(productRepository.save(anyObject(Product::class.java))).thenReturn(expectedProduct)

        val result = createProductUseCase.execute(input)

        Assertions.assertTrue(result is Result.Success)

    }

}

