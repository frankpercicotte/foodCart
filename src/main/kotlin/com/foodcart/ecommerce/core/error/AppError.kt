package com.foodcart.ecommerce.core.error

import org.springframework.http.HttpStatus

open class AppError protected constructor(
    val code: String,
    override val message: String,
    val context: Map<String, Any>
) : RuntimeException(message) {

    companion object {
        fun of(code: AppError, vararg args: Any?, context: Map<String, Any> = emptyMap()): AppError {
            val msg = String.format(code.message, *args.map { it.toString() }.toTypedArray())
            return AppError(code.code, msg, context)
        }
    }
}