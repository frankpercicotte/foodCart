package com.foodcart.ecommerce.adapters.inbound.http.common.dto

data class ErrorResponse(
    val code: String,
    val message: String,
    val context: Map<String, Any> = emptyMap()
)