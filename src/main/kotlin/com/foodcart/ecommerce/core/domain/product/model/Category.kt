package com.foodcart.ecommerce.core.domain.product.model

import com.foodcart.ecommerce.core.error.ErrorCode
import com.foodcart.ecommerce.core.error.ErrorMessages
import java.math.BigDecimal

class Category(
    val id: Long,
    val name: String,
    val profitMargin: BigDecimal,
    val maxDiscount: BigDecimal,
    val isActive: Boolean = true
){
    init {
        require(name.isNotBlank()){
            ErrorMessages.CATEGORY_NAME_NOT_BLANK
        }
        require(profitMargin >= BigDecimal.ZERO){
            ErrorCode.PROFIT_MARGIN_NEGATIVE.message.format(profitMargin.toDouble())
        }
        require(maxDiscount >= BigDecimal.ZERO) {
            ErrorCode.MAXIMUS_DISCOUNT_NOT_NEGATIVE.message.format(maxDiscount.toDouble())
        }
        require(maxDiscount <= BigDecimal(100)){
            ErrorCode.INVALID_DISCOUNT_MAX_PERCENTAGE.message.format(maxDiscount.toDouble())
        }
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "Category(id=$id, name='$name', profitMargin=$profitMargin%, maxDiscount=$maxDiscount%, active=$isActive)"
    }

}
