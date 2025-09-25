package com.foodcart.ecommerce.core.domain.product.service

import com.foodcart.ecommerce.core.domain.common.exception.InactiveCategoryException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidDiscountPercentageException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidPriceException
import com.foodcart.ecommerce.core.domain.common.exception.NegativeDiscountPercentageException
import com.foodcart.ecommerce.core.domain.product.model.Category
import java.math.BigDecimal
import java.math.RoundingMode


class CategoryDiscountService {
    fun validateDiscount(category: Category, discountPercentage: BigDecimal) {
        when {
            !category.isActive -> throw InactiveCategoryException(category.name)
            discountPercentage < BigDecimal.ZERO -> throw NegativeDiscountPercentageException(discountPercentage)
            discountPercentage > category.maxDiscount -> throw InvalidDiscountPercentageException(
                discountPercentage = discountPercentage,
                maxAllowed = category.maxDiscount,
                categoryName = category.name
            )
        }
    }

    fun calculateDiscountAmount(
        category: Category,
        price: BigDecimal,
        discountPercentage: BigDecimal
    ): BigDecimal {
        if (price < BigDecimal.ZERO) {
            throw InvalidPriceException(price)
        }
        validateDiscount(category, discountPercentage)
        return price
            .multiply(discountPercentage)
            .divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
    }

    fun applyDiscount(
        category: Category,
        price: BigDecimal,
        discountPercentage: BigDecimal
    ): BigDecimal =
        price.subtract(calculateDiscountAmount(category, price, discountPercentage)).setScale(2, RoundingMode.HALF_UP)

    fun getMaximumDiscountAllowed(category: Category): BigDecimal =
        if (category.isActive) category.maxDiscount else throw InactiveCategoryException(category.name)

    fun applyDiscountWithDetails(
        category: Category,
        price: BigDecimal,
        discountPercentage: BigDecimal
    ): DiscountResult {
        val discountAmount = calculateDiscountAmount(category, price, discountPercentage)
        val finalPrice = price.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP)
        return DiscountResult(
            originalPrice = price,
            discountPercentage = discountPercentage,
            discountAmount = discountAmount,
            finalPrice = finalPrice,
            categoryName = category.name
        )
    }

    fun canApplyDiscount(category: Category, discountPercentage: BigDecimal): Boolean {
        return try {
            validateDiscount(category, discountPercentage)
            true
        } catch (e: Exception) {
            false
        }
    }

    data class DiscountResult(
        val originalPrice: BigDecimal,
        val discountPercentage: BigDecimal,
        val discountAmount: BigDecimal,
        val finalPrice: BigDecimal,
        val categoryName: String
    ) {
        fun getSavingsMessage(): String {
            return "You saved $discountAmount ($discountPercentage%) on $categoryName"
        }
    }

}

