package com.foodcart.ecommerce.core.domain.product.service

import com.foodcart.ecommerce.core.domain.common.exception.InactiveCategoryException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidCostException
import com.foodcart.ecommerce.core.domain.product.model.Category
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import java.math.BigDecimal

class CategoryPricingServiceTest {

    private val pricingService = CategoryPricingService()
    private val activeCategory = Category(1L, "Food", BigDecimal("30.0"), BigDecimal("10.0"), true)
    private val inactiveCategory = Category(2L, "Inactive", BigDecimal("30.0"), BigDecimal("10.0"), false)

    @Test
    fun `should calculate final price correctly for valid cost`() {
        val cost = BigDecimal("100.0")

        val result = pricingService.calculateFinalPrice(activeCategory, cost)

        assertEquals(BigDecimal("130.00"), result)
    }

    @Test
    fun `should return error for negative cost`() {
        val negativeCost = BigDecimal("-10.0")

        assertThrows<InvalidCostException> {
            pricingService.calculateFinalPrice(activeCategory, negativeCost)
        }
    }

    @Test
    fun `should return error for inactive category`() {
        val cost = BigDecimal("100.0")

        assertThrows<InactiveCategoryException> {
            pricingService.calculateFinalPrice(inactiveCategory, cost)
        }
    }

    @Test
    fun `should calculate margin amount correctly`() {
        val cost = BigDecimal("50.0")

        val result = pricingService.calculateMarginAmount(activeCategory, cost)

        assertEquals(BigDecimal("15.00"), result)
    }

    @Test
    fun `should calculate actual margin percentage correctly`() {
        val cost = BigDecimal("100.0")
        val finalPrice = BigDecimal("130.0")

        val result = pricingService.calculateActualMarginPercentage(cost, finalPrice)

        assertEquals(BigDecimal("30.00"), result)
    }

    @Test
    fun `should return error for zero cost in margin calculation`() {
        val zeroCost = BigDecimal.ZERO
        val finalPrice = BigDecimal("100.0")

        assertThrows<InvalidCostException> {
            pricingService.calculateActualMarginPercentage(zeroCost, finalPrice)
        }
    }
}