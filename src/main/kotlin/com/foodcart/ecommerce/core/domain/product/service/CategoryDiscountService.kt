package com.foodcart.ecommerce.core.domain.product.service

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.shared.Result
import com.foodcart.ecommerce.core.domain.product.model.Category
import java.math.BigDecimal
import java.math.RoundingMode


class CategoryDiscountService {
    fun validateDiscount(category: Category, discountPercentage: BigDecimal): Result<Unit, ProductError> {
        return when {
            !category.isActive -> Result.Failure(ProductError.InactiveCategory(category.name))

            discountPercentage < BigDecimal.ZERO -> Result.Failure(
                ProductError.NegativeDiscountPercentage(discountPercentage)
            )

            discountPercentage > category.maxDiscount -> Result.Failure(
                ProductError.InvalidDiscountPercentage(
                    discountPercentage = discountPercentage,
                    maxAllowed = category.maxDiscount,
                    categoryName = category.name
                )
            )

            else -> Result.Success(Unit)
        }
    }

    fun calculateDiscountAmount(
        category: Category,
        price: BigDecimal,
        discountPercentage: BigDecimal
    ): Result<BigDecimal, ProductError> {
        if (price < BigDecimal.ZERO) {
            return Result.Failure(ProductError.InvalidPrice(price))
        }
        return validateDiscount(category, discountPercentage).map {
            price
                .multiply(discountPercentage)
                .divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
        }
    }

    fun applyDiscount(
        category: Category,
        price: BigDecimal,
        discountPercentage: BigDecimal
    ): Result<BigDecimal, ProductError> {
        return calculateDiscountAmount(category, price, discountPercentage).map { discountAmount ->
            price.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP)
        }
    }

    fun getMaximumDiscountAllowed(category: Category): Result<BigDecimal, ProductError> {
        return if (category.isActive) {
            Result.Success(category.maxDiscount)
        } else {
            Result.Failure(ProductError.InactiveCategory(category.name))
        }
    }

    fun applyDiscountWithDetails(
        category: Category,
        price: BigDecimal,
        discountPercentage: BigDecimal
    ): Result<DiscountResult, ProductError> {
        return calculateDiscountAmount(category, price, discountPercentage).map { discountAmount ->
            val finalPrice = price.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP)
            DiscountResult(
                originalPrice = price,
                discountPercentage = discountPercentage,
                discountAmount = discountAmount,
                finalPrice = finalPrice,
                categoryName = category.name
            )
        }
    }

    fun canApplyDiscount(category: Category, discountPercentage: BigDecimal): Boolean {
        return validateDiscount(category, discountPercentage).isSuccess()
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

