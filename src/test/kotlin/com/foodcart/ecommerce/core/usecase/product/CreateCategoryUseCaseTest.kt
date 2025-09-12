package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.exception.CategoryAlreadyExistsException
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
            input.maxDiscount,
            isActive = true
        )

        `when`(categoryRepository.existsByName(input.name)).thenReturn(false)
        `when`(categoryRepository.save(anyObject(Category::class.java))).thenReturn(expectedCategory)

        val result = createCategoryUseCaseImpl.execute(input)

        assertEquals(expectedCategory, result)
        verify(categoryRepository).existsByName(input.name)
        verify(categoryRepository).save(anyObject(Category::class.java))
    }

    @Test
    fun `should throw CategoryAlreadyExistsException when category name already exists`() {
        val input = CreateCategoryUseCase.Input(
            name = "Electronics",
            profitMargin = BigDecimal("0.20"),
            maxDiscount = BigDecimal("0.10")
        )

        `when`(categoryRepository.existsByName(input.name)).thenReturn(true)

        val exception = assertThrows<CategoryAlreadyExistsException> {
            createCategoryUseCaseImpl.execute(input)
        }

        assertEquals("Electronics", exception.categoryName)
        verify(categoryRepository).existsByName(input.name)
        verify(categoryRepository, never()).save(anyObject(Category::class.java))
    }
}