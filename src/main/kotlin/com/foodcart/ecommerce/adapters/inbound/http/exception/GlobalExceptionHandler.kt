package com.foodcart.ecommerce.adapters.inbound.http.exception

import com.foodcart.ecommerce.core.domain.common.exception.CategoryAlreadyExistsException
import com.foodcart.ecommerce.core.domain.common.exception.CategoryNotFoundException
import com.foodcart.ecommerce.core.domain.common.exception.DatabaseOperationException
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
