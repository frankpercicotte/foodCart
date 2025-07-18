package com.foodcart.ecommerce.core.domain.product.service

import com.foodcart.ecommerce.core.domain.common.DomainError
import com.foodcart.ecommerce.core.domain.common.Result
import com.foodcart.ecommerce.core.domain.product.model.Category
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import java.math.BigDecimal

class CategoryDiscountServiceTest {

    private val discountService = CategoryDiscountService()
    private val activeCategory = Category(1L, "Food", BigDecimal("30.0"), BigDecimal("15.0"), true)
    private val inactiveCategory = Category(2L, "Inactive", BigDecimal("30.0"), BigDecimal("15.0"), false)

    @Test
    fun `should apply discount correctly for valid parameters`() {
        val price = BigDecimal("100.0")
        val discount = BigDecimal("10.0")

        val result = discountService.applyDiscount(activeCategory, price, discount)

        assertTrue(result.isSuccess())
        assertEquals(BigDecimal("90.00"), result.getOrNull()) // 100 - 10% = 90
    }

    @Test
    fun `should return error for discount exceeding maximum`() {
        val price = BigDecimal("100.0")
        val excessiveDiscount = BigDecimal("20.0") // Máximo é 15%

        val result = discountService.applyDiscount(activeCategory, price, excessiveDiscount)

        assertTrue(result.isFailure())
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is DomainError.InvalidDiscountPercentage)
        assertEquals("INVALID_DISCOUNT_PERCENTAGE", result.error.code)
    }

    @Test
    fun `should return error for negative discount`() {
        val price = BigDecimal("100.0")
        val negativeDiscount = BigDecimal("-5.0")

        val result = discountService.validateDiscount(activeCategory, negativeDiscount)

        assertTrue(result.isFailure())
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is DomainError.NegativeDiscountPercentage)
    }

    @Test
    fun `should return error for inactive category`() {
        val price = BigDecimal("100.0")
        val discount = BigDecimal("10.0")

        val result = discountService.applyDiscount(inactiveCategory, price, discount)

        assertTrue(result.isFailure())
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is DomainError.InactiveCategory)
    }

    @Test
    fun `should calculate discount amount correctly`() {
        val price = BigDecimal("200.0")
        val discount = BigDecimal("10.0")

        val result = discountService.calculateDiscountAmount(activeCategory, price, discount)

        assertTrue(result.isSuccess())
        assertEquals(BigDecimal("20.00"), result.getOrNull()) // 200 * 10% = 20
    }

    @Test
    fun `should return detailed discount result`() {
        val price = BigDecimal("100.0")
        val discount = BigDecimal("15.0")

        val result = discountService.applyDiscountWithDetails(activeCategory, price, discount)

        assertTrue(result.isSuccess())
        val details = result.getOrNull()!!
        assertEquals(BigDecimal("100.0"), details.originalPrice)
        assertEquals(BigDecimal("15.0"), details.discountPercentage)
        assertEquals(BigDecimal("15.00"), details.discountAmount)
        assertEquals(BigDecimal("85.00"), details.finalPrice)
        assertEquals("Food", details.categoryName)
    }

    @Test
    fun `should validate discount permission correctly`() {
        assertTrue(discountService.canApplyDiscount(activeCategory, BigDecimal("10.0")))  // Válido
        assertTrue(discountService.canApplyDiscount(activeCategory, BigDecimal("15.0")))  // No limite
        assertFalse(discountService.canApplyDiscount(activeCategory, BigDecimal("20.0"))) // Acima do limite
        assertFalse(discountService.canApplyDiscount(inactiveCategory, BigDecimal("10.0"))) // Categoria inativa
    }
}