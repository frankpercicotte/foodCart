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
    private lateinit var createCategoryUseCase: CreateCategoryUseCase

    // Helper function to avoid Mockito nullability issues with Kotlin
    private fun <T> anyObject(type: Class<T>): T = any(type)
    private fun anyStringValue(): String = anyString()

    @Test
    fun `should create category successfully when all data is valid`() {
        val request = CreateCategoryRequest(
            name = "Electronics",
            profitMargin = BigDecimal("0.20"),
            maxDiscount = BigDecimal("0.10")
        )
        val expectedCategory = Category(
            1L,
            request.name,
            request.profitMargin,
            request.maxDiscount
        )

        `when`(categoryRepository.existsByName(request.name)).thenReturn(false)
        `when`(categoryRepository.save(anyObject(Category::class.java))).thenReturn(expectedCategory)

        val result = createCategoryUseCase.execute(request)

        assertTrue(result is Result.Success)
        assertEquals(expectedCategory, (result as Result.Success).value)
        verify(categoryRepository).existsByName(request.name)
        verify(categoryRepository).save(anyObject(Category::class.java))
    }

    @Test
    fun `should fail when category name already exists`() {
        val request = CreateCategoryRequest(
            name = "Electronics",
            profitMargin = BigDecimal("0.20"),
            maxDiscount = BigDecimal("0.10")
        )

        `when`(categoryRepository.existsByName(request.name)).thenReturn(true)

        val result = createCategoryUseCase.execute(request)

        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is ProductError.CategoryNameAlreadyExists)
        verify(categoryRepository).existsByName(request.name)
        verify(categoryRepository, never()).save(anyObject(Category::class.java))
    }

    @Test
    fun `should fail when category name is blank`() {
        val request = CreateCategoryRequest(
            name = "   ",
            profitMargin = BigDecimal("0.20"),
            maxDiscount = BigDecimal("0.10")
        )

        val result = createCategoryUseCase.execute(request)

        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is ProductError.InvalidCategoryName)
        verify(categoryRepository, never()).existsByName(anyStringValue())
        verify(categoryRepository, never()).save(anyObject(Category::class.java))
    }

    @Test
    fun `should fail when profit margin is negative`() {
        val request = CreateCategoryRequest(
            name = "Electronics",
            profitMargin = BigDecimal("-0.10"),
            maxDiscount = BigDecimal("0.10")
        )

        val result = createCategoryUseCase.execute(request)

        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is ProductError.InvalidProfitMargin)
        verify(categoryRepository, never()).existsByName(anyStringValue())
        verify(categoryRepository, never()).save(anyObject(Category::class.java))
    }

    @Test
    fun `should fail when max discount is negative`() {
        val request = CreateCategoryRequest(
            name = "Electronics",
            profitMargin = BigDecimal("0.20"),
            maxDiscount = BigDecimal("-0.05")
        )

        val result = createCategoryUseCase.execute(request)

        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is ProductError.InvalidMaxDiscount)
        verify(categoryRepository, never()).existsByName(anyStringValue())
        verify(categoryRepository, never()).save(anyObject(Category::class.java))
    }
}