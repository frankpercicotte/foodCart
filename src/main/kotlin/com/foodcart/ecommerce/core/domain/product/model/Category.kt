package com.foodcart.ecommerce.core.domain.product.model

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.shared.Result
import java.math.BigDecimal

class Category(
    val categoryId: String? = null,
    val name: String,
    val profitMargin: BigDecimal,
    val maxDiscount: BigDecimal,
    val isActive: Boolean = true
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false
        return categoryId == other.categoryId
    }

    override fun hashCode(): Int = categoryId.hashCode()

    override fun toString(): String {
        return "Category(id=$categoryId, name='$name', profitMargin=$profitMargin%, maxDiscount=$maxDiscount%, active=$isActive)"
    }

    companion object {
        fun create(
            id: Long,
            name: String,
            profitMargin: BigDecimal,
            maxDiscount: BigDecimal,
            isActive: Boolean = true
        ): Result<Category, ProductError> {
            if (name.isBlank()) {
                return Result.Failure(ProductError.InvalidCategoryName(name))
            }
            if (profitMargin < BigDecimal.ZERO) {
                return Result.Failure(ProductError.InvalidProfitMargin(profitMargin))
            }
            if (maxDiscount < BigDecimal.ZERO) {
                return Result.Failure(ProductError.InvalidMaxDiscount(maxDiscount))
            }
            if (maxDiscount > BigDecimal(100)) {
                return Result.Failure(ProductError.MaxDiscountExceeded(maxDiscount))
            }
            return Result.Success(
                Category(
                    categoryId = id,
                    name = name,
                    profitMargin = profitMargin,
                    maxDiscount = maxDiscount,
                    isActive = isActive
                )
            )
        }


        fun update(
            existingCategory: Category,
            name: String = existingCategory.name,
            profitMargin: BigDecimal = existingCategory.profitMargin,
            maxDiscount: BigDecimal = existingCategory.maxDiscount,
            isActive: Boolean = existingCategory.isActive
        ): Result<Category, ProductError> {
            return create(
                id = existingCategory.categoryId,
                name = name,
                profitMargin = profitMargin,
                maxDiscount = maxDiscount,
                isActive = isActive
            )
        }
    }

}
