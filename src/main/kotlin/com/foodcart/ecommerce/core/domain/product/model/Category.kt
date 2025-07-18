package com.foodcart.ecommerce.core.domain.product.model

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
            "Category name cannot be blank"
        }
        require(profitMargin >= BigDecimal.ZERO){
            "Profit margin cannot be negative: $profitMargin"
        }
        require(maxDiscount >= BigDecimal.ZERO) {
            "Max discount cannot be negative: $maxDiscount"
        }
        require(maxDiscount <= BigDecimal(100)){
            "Max discount cannot exceed 100%: $maxDiscount"
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
