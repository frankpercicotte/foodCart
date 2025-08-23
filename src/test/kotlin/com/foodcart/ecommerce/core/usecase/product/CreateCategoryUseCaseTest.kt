package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.shared.Result
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class CreateCategoryUseCaseTest {

    @Mock
    private lateinit var categoryRepository: CategoryRepository

    @InjectMocks
    private lateinit var createCategoryUseCaseImpl: CreateCategoryUseCaseImpl

    // Helper function to avoid Mockito nullability issues with Kotlin
    private fun <T> anyObject(type: Class<T>): T = any(type)

    @Test
    fun `should create category successfully when all data is valid`() {

        val input = CreateCategoryUseCase.Input(
            name = "Electronics",
            profitMargin = BigDecimal("0.20"),
            maxDiscount = BigDecimal("0.10")
        )
        val expectedCategory = Category(
            1L,
            input.name,
            input.profitMargin,
            input.maxDiscount
        )

        `when`(categoryRepository.existsByName(input.name)).thenReturn(false)
        `when`(categoryRepository.save(anyObject(Category::class.java))).thenReturn(expectedCategory)

        val result = createCategoryUseCaseImpl.execute(input)

        assertTrue(result is Result.Success)
        assertEquals(expectedCategory, (result as Result.Success).value)
        verify(categoryRepository).existsByName(input.name)
        verify(categoryRepository).save(anyObject(Category::class.java))
    }

    @Test
    fun `should fail when category name already exists`() {
        val input = CreateCategoryUseCase.Input(
            name = "Electronics",
            profitMargin = BigDecimal("0.20"),
            maxDiscount = BigDecimal("0.10")
        )

        `when`(categoryRepository.existsByName(input.name)).thenReturn(true)

        val result = createCategoryUseCaseImpl.execute(input)

        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is ProductError.CategoryNameAlreadyExists)
        verify(categoryRepository).existsByName(input.name)
        verify(categoryRepository, never()).save(anyObject(Category::class.java))
    }
}