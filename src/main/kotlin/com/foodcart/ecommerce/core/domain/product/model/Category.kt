package com.foodcart.ecommerce.core.domain.product.model

import com.foodcart.ecommerce.core.domain.common.exception.InvalidCategoryNameException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidMaxDiscountException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidProfitMarginException
import com.foodcart.ecommerce.core.domain.common.exception.MaxDiscountExceededException
import java.math.BigDecimal

class Category(
    val categoryId: Long? = null,
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

    override fun hashCode(): Int = categoryId?.hashCode() ?: 0

    override fun toString(): String {
        return "Category(id=$categoryId, name='$name', profitMargin=$profitMargin%, maxDiscount=$maxDiscount%, active=$isActive)"
    }

    companion object {
        fun create(
            id: Long?,
            name: String,
            profitMargin: BigDecimal,
            maxDiscount: BigDecimal,
            isActive: Boolean = true
        ): Category {
            if (name.isBlank()) {
                throw InvalidCategoryNameException(name)
            }
            if (profitMargin < BigDecimal.ZERO) {
                throw InvalidProfitMarginException(profitMargin)
            }
            if (maxDiscount < BigDecimal.ZERO) {
                throw InvalidMaxDiscountException(maxDiscount)
            }
            if (maxDiscount > BigDecimal(100)) {
                throw MaxDiscountExceededException(maxDiscount)
            }
            return Category(
                categoryId = id,
                name = name,
                profitMargin = profitMargin,
                maxDiscount = maxDiscount,
                isActive = isActive
            )
        }


        fun update(
            existingCategory: Category,
            name: String = existingCategory.name,
            profitMargin: BigDecimal = existingCategory.profitMargin,
            maxDiscount: BigDecimal = existingCategory.maxDiscount,
            isActive: Boolean = existingCategory.isActive
        ): Category {
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
