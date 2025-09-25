package com.foodcart.ecommerce.adapters.inbound.http.exception

import com.foodcart.ecommerce.core.domain.common.exception.CategoryAlreadyExistsException
import com.foodcart.ecommerce.core.domain.common.exception.CategoryNotFoundException
import com.foodcart.ecommerce.core.domain.common.exception.DatabaseOperationException
import com.foodcart.ecommerce.core.domain.common.exception.InactiveCategoryException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidPriceException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidProductNameException
import com.foodcart.ecommerce.core.domain.common.exception.ProductNameAlreadyExistsException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidCategoryNameException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidProfitMarginException
import com.foodcart.ecommerce.core.domain.common.exception.InvalidMaxDiscountException
import com.foodcart.ecommerce.core.domain.common.exception.MaxDiscountExceededException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val errors: List<FieldErrorResponse> = emptyList()
)

data class FieldErrorResponse(
    val field: String,
    val message: String,
    val rejectedValue: Any?
)

@ControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.allErrors.map { error ->
            val fieldName = (error as? FieldError)?.field ?: ""
            val errorMessage = error.defaultMessage ?: ""
            val rejectedValue = (error as? FieldError)?.rejectedValue
            
            FieldErrorResponse(
                field = fieldName,
                message = errorMessage,
                rejectedValue = rejectedValue
            )
        }

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Error",
            message = "Validation failed for request",
            path = request.getDescription(false).substring(4), // Remove "uri=" prefix
            errors = errors
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(CategoryAlreadyExistsException::class)
    fun handleCategoryAlreadyExists(e: CategoryAlreadyExistsException): ResponseEntity<Map<String, Any>> {
        logger.warn("Category already exists: {}", e.message)
        val body = mapOf(
            "code" to "CATEGORY_ALREADY_EXISTS",
            "message" to (e.message ?: "Category already exists"),
            "name" to e.categoryName
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body)
    }

    @ExceptionHandler(CategoryNotFoundException::class)
    fun handleCategoryNotFound(e: CategoryNotFoundException): ResponseEntity<Map<String, Any>> {
        logger.warn("Category not found: {}", e.message)
        val body = mapOf(
            "code" to "CATEGORY_NOT_FOUND",
            "message" to (e.message ?: "Category not found"),
            "categoryId" to e.categoryId
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

    @ExceptionHandler(DatabaseOperationException::class)
    fun handleDatabaseOperation(e: DatabaseOperationException): ResponseEntity<Map<String, Any>> {
        logger.error("Database error on {}: {}", e.operation, e.message, e)
        val body = mapOf(
            "code" to "DATABASE_ERROR",
            "message" to (e.message ?: "Failed to execute database operation: ${e.operation}"),
            "operation" to e.operation
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }

    @ExceptionHandler(ProductNameAlreadyExistsException::class)
    fun handleProductNameAlreadyExists(e: ProductNameAlreadyExistsException): ResponseEntity<Map<String, Any>> {
        logger.warn("Product name already exists: {}", e.message)
        val body = mapOf(
            "code" to "PRODUCT_NAME_ALREADY_EXISTS",
            "message" to (e.message ?: "Product name already exists"),
            "name" to e.name
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body)
    }

    @ExceptionHandler(InvalidProductNameException::class)
    fun handleInvalidProductName(e: InvalidProductNameException): ResponseEntity<Map<String, Any>> {
        logger.warn("Invalid product name: {}", e.message)
        val body = mapOf(
            "code" to "INVALID_PRODUCT_NAME",
            "message" to (e.message ?: "Invalid product name"),
            "name" to e.name
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(InvalidPriceException::class)
    fun handleInvalidPrice(e: InvalidPriceException): ResponseEntity<Map<String, Any>> {
        logger.warn("Invalid product price: {}", e.message)
        val body = mapOf(
            "code" to "INVALID_PRODUCT_PRICE",
            "message" to (e.message ?: "Invalid product price"),
            "price" to e.price
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(InactiveCategoryException::class)
    fun handleInactiveCategory(e: InactiveCategoryException): ResponseEntity<Map<String, Any>> {
        logger.warn("Inactive category: {}", e.message)
        val body = mapOf(
            "code" to "INACTIVE_CATEGORY",
            "message" to (e.message ?: "Inactive category"),
            "categoryName" to e.categoryName
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(InvalidCategoryNameException::class)
    fun handleInvalidCategoryName(e: InvalidCategoryNameException): ResponseEntity<Map<String, Any>> {
        logger.warn("Invalid category name: {}", e.message)
        val body = mapOf(
            "code" to "INVALID_CATEGORY_NAME",
            "message" to (e.message ?: "Invalid category name"),
            "name" to e.name
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(InvalidProfitMarginException::class)
    fun handleInvalidProfitMargin(e: InvalidProfitMarginException): ResponseEntity<Map<String, Any>> {
        logger.warn("Invalid profit margin: {}", e.message)
        val body = mapOf(
            "code" to "INVALID_PROFIT_MARGIN",
            "message" to (e.message ?: "Invalid profit margin"),
            "profitMargin" to e.profitMargin
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(InvalidMaxDiscountException::class)
    fun handleInvalidMaxDiscount(e: InvalidMaxDiscountException): ResponseEntity<Map<String, Any>> {
        logger.warn("Invalid max discount: {}", e.message)
        val body = mapOf(
            "code" to "INVALID_MAX_DISCOUNT",
            "message" to (e.message ?: "Invalid max discount"),
            "maxDiscount" to e.maxDiscount
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(MaxDiscountExceededException::class)
    fun handleMaxDiscountExceeded(e: MaxDiscountExceededException): ResponseEntity<Map<String, Any>> {
        logger.warn("Max discount exceeded: {}", e.message)
        val body = mapOf(
            "code" to "MAX_DISCOUNT_EXCEEDED",
            "message" to (e.message ?: "Max discount exceeded"),
            "maxDiscount" to e.maxDiscount
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(e: Exception): ResponseEntity<Map<String, Any>> {
        logger.error("Unexpected error: {}", e.message, e)
        val body = mapOf(
            "code" to "INTERNAL_SERVER_ERROR",
            "message" to "An unexpected error occurred. Please try again later."
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}
