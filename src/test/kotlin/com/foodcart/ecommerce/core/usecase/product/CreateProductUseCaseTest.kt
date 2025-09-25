package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import com.foodcart.ecommerce.core.domain.product.service.CategoryPricingService
import com.foodcart.ecommerce.core.domain.common.exception.ProductNameAlreadyExistsException
import com.foodcart.ecommerce.core.samples.CreateProductInputSample
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class CreateProductUseCaseTest {
    @InjectMocks
    private lateinit var createProductUseCase: CreateProductUseCaseImpl

    @Mock
    private lateinit var productRepository: ProductRepository
    @Mock
    private lateinit var categoryRepository: CategoryRepository
    @Mock
    private lateinit var categoryPricingService: CategoryPricingService


    private fun <T> anyObject(type: Class<T>): T = any(type)

    @Test
    fun `should create a new product successfully`(){
        val input = CreateProductInputSample.createProductInput()
        val finalPrice = BigDecimal("10.00")

        val expectedCategory = Category(
            categoryId = input.categoryId.toLong(),
            name = "Arts",
            profitMargin = BigDecimal("0.20"),
            maxDiscount = BigDecimal("0.10")
        )

        val expectedProduct = Product(
            productId = null,
            name = input.name,
            normalizedName = input.name,
            description = input.description,
            price = finalPrice,
            cost = input.cost,
            discount = input.discount,
            categoryId = input.categoryId,
            stockQuantity = input.stockQuantity,
            isActive = input.isActive,
            imageUrl = input.imageUrl,
        )

        `when`(categoryRepository.findById(input.categoryId.toLong())).thenReturn(expectedCategory)
        `when`(categoryPricingService.calculateFinalPriceOne(expectedCategory, input.cost)).thenReturn(finalPrice)
        `when`(productRepository.save(anyObject(Product::class.java))).thenReturn(expectedProduct)

        val result = createProductUseCase.execute(input)

        Assertions.assertEquals(finalPrice, result.price)

    }

    @Test
    fun `should not create a new product when name already exist`(){
        val input = CreateProductInputSample.createProductInput()
        val normalizedName = input.name.lowercase()

        `when`( productRepository.existsByNormalizedNameAndCategoryId(normalizedName, input.categoryId)).thenReturn(true)

        assertThrows(ProductNameAlreadyExistsException::class.java) {
            createProductUseCase.execute(input)
        }
        verify(productRepository).existsByNormalizedNameAndCategoryId(normalizedName, input.categoryId)
        verify(productRepository, never()).save(anyObject(Product::class.java))
    }

}

