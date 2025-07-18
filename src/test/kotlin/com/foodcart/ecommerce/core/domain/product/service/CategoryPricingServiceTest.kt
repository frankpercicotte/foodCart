package com.foodcart.ecommerce.core.domain.product.service

import com.foodcart.ecommerce.core.domain.common.DomainError
import com.foodcart.ecommerce.core.domain.common.Result
import com.foodcart.ecommerce.core.domain.product.model.Category
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.math.BigDecimal

class CategoryPricingServiceTest {

    private val pricingService = CategoryPricingService()
    private val activeCategory = Category(1L, "Food", BigDecimal("30.0"), BigDecimal("10.0"), true)
    private val inactiveCategory = Category(2L, "Inactive", BigDecimal("30.0"), BigDecimal("10.0"), false)

    @Test
    fun `should calculate final price correctly for valid cost`() {
        val cost = BigDecimal("100.0")

        val result = pricingService.calculateFinalPrice(activeCategory, cost)

        assertTrue(result.isSuccess())
        assertEquals(BigDecimal("130.00"), result.getOrNull()) // 100 + 30% = 130
    }

    @Test
    fun `should return error for negative cost`() {
        val negativeCost = BigDecimal("-10.0")

        val result = pricingService.calculateFinalPrice(activeCategory, negativeCost)

        assertTrue(result.isFailure())
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is DomainError.InvalidCost)
        assertEquals("INVALID_COST", result.error.code)
    }

    @Test
    fun `should return error for inactive category`() {
        val cost = BigDecimal("100.0")

        val result = pricingService.calculateFinalPrice(inactiveCategory, cost)

        assertTrue(result.isFailure())
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is DomainError.InactiveCategory)
        assertEquals("INACTIVE_CATEGORY", result.error.code)
    }

    @Test
    fun `should calculate margin amount correctly`() {
        val cost = BigDecimal("50.0")

        val result = pricingService.calculateMarginAmount(activeCategory, cost)

        assertTrue(result.isSuccess())
        assertEquals(BigDecimal("15.00"), result.getOrNull()) // 50 * 30% = 15
    }

    @Test
    fun `should handle batch calculation with mixed results`() {
        val costs = listOf(
            BigDecimal("100.0"),  // Válido
            BigDecimal("-10.0"),  // Inválido
            BigDecimal("50.0")    // Válido
        )

        val result = pricingService.calculateFinalPrices(activeCategory, costs)

        assertTrue(result.isFailure())
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is DomainError.CalculationError)
    }

    @Test
    fun `should calculate actual margin percentage correctly`() {
        val cost = BigDecimal("100.0")
        val finalPrice = BigDecimal("130.0")

        val result = pricingService.calculateActualMarginPercentage(cost, finalPrice)

        assertTrue(result.isSuccess())
        assertEquals(BigDecimal("30.00"), result.getOrNull()) // (130-100)/100 * 100 = 30%
    }

    @Test
    fun `should return error for zero cost in margin calculation`() {
        val zeroCost = BigDecimal.ZERO
        val finalPrice = BigDecimal("100.0")

        val result = pricingService.calculateActualMarginPercentage(zeroCost, finalPrice)

        assertTrue(result.isFailure())
        assertTrue(result is Result.Failure)
        assertTrue((result as Result.Failure).error is DomainError.ZeroCost)
    }
}