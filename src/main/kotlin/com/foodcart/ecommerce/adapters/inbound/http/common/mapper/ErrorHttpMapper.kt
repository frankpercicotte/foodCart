package com.foodcart.ecommerce.adapters.inbound.http.common.mapper

import com.foodcart.ecommerce.adapters.inbound.http.common.dto.ErrorResponse
import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.error.AppError
import org.springframework.http.HttpStatus

fun ProductError.toHttp(): Pair<HttpStatus, ErrorResponse> =
    when (this) {
        is ProductError.ProductNameAlreadyExists ->
            HttpStatus.CONFLICT to this.toErrorResponse()
        is ProductError.CategoryNotFound ->
            HttpStatus.NOT_FOUND to this.toErrorResponse()
        is ProductError.InactiveCategory ->
            HttpStatus.UNPROCESSABLE_ENTITY to this.toErrorResponse()
        is ProductError.InvalidPrice ->
            HttpStatus.BAD_REQUEST to this.toErrorResponse()
        is ProductError.InvalidCost ->
            HttpStatus.BAD_REQUEST to this.toErrorResponse()
        is ProductError.NegativeDiscountPercentage ->
            HttpStatus.BAD_REQUEST to this.toErrorResponse()
        is ProductError.InvalidDiscountPercentage ->
            HttpStatus.BAD_REQUEST to this.toErrorResponse()
        is ProductError.InvalidProfitMargin ->
            HttpStatus.BAD_REQUEST to this.toErrorResponse()
        is ProductError.InvalidMaxDiscount ->
            HttpStatus.BAD_REQUEST to this.toErrorResponse()
        is ProductError.MaxDiscountExceeded ->
            HttpStatus.UNPROCESSABLE_ENTITY to this.toErrorResponse()
        is ProductError.InvalidCategoryName ->
            HttpStatus.BAD_REQUEST to this.toErrorResponse()
        is ProductError.CategoryNameAlreadyExists ->
            HttpStatus.CONFLICT to this.toErrorResponse()
        is ProductError.CalculationError ->
            HttpStatus.UNPROCESSABLE_ENTITY to this.toErrorResponse()
        is ProductError.DatabaseError ->
            HttpStatus.INTERNAL_SERVER_ERROR to this.toErrorResponse()
    }

fun AppError.toErrorResponse(): ErrorResponse =
    ErrorResponse(
        code = this.code,
        message = this.message,
        context = this.context
    )