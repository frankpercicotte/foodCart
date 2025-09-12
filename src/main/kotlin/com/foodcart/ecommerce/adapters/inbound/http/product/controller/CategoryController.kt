package com.foodcart.ecommerce.adapters.inbound.http.product.controller

import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateCategoryRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toCreateResponse
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toInput
import com.foodcart.ecommerce.core.domain.common.exception.CategoryAlreadyExistsException
import com.foodcart.ecommerce.core.domain.common.exception.CategoryNotFoundException
import com.foodcart.ecommerce.core.domain.common.exception.DatabaseOperationException
import com.foodcart.ecommerce.core.usecase.product.CreateCategoryUseCase
import com.foodcart.ecommerce.core.usecase.product.GetCategoryUseCase
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(CategoryController::class.java)
    }

    @PostMapping
    fun create(@Valid @RequestBody request: CreateCategoryRequest): ResponseEntity<Any> {
        logger.info(
            "POST /api/v1/categories - Received request: {}, {}, {}, {}",
            request.name, request.profitMargin, request.maxDiscount, request.isActive
        )
        
        return try {
            val category = createCategoryUseCase.execute(request.toInput())
            val body = category.toCreateResponse()
            logger.info("POST /api/v1/categories - Success: categoryId={}, name={}", category.categoryId, category.name)
            ResponseEntity.status(HttpStatus.CREATED).body(body)
        } catch (e: CategoryAlreadyExistsException) {
            logger.warn("POST /api/v1/categories - Category already exists: {}", e.message)
            ResponseEntity.status(HttpStatus.CONFLICT)
                .body(createErrorResponse(
                    code = "CATEGORY_ALREADY_EXISTS",
                    message = e.message ?: "Category already exists",
                    details = mapOf("name" to e.categoryName)
                ))
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<Any> {
        logger.info("GET /api/v1/categories - getAll")
        return try {
            val categories = getCategoryUseCase.findAll()
            ResponseEntity.status(HttpStatus.OK).body(categories)
        } catch (e: DatabaseOperationException) {
            logger.error("GET /api/v1/categories - Database error: {}", e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(
                    code = "DATABASE_ERROR",
                    message = "Failed to fetch categories. Please try again later.",
                    details = mapOf("operation" to e.operation)
                ))
        } catch (e: Exception) {
            logger.error("GET /api/v1/categories - Unexpected error: {}", e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(
                    code = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred. Please try again later."
                ))
        }
    }
    
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Any> {
        logger.info("GET /api/v1/categories/{} - getById", id)
        return try {
            val category = getCategoryUseCase.findById(id)
            ResponseEntity.status(HttpStatus.OK).body(category)
        } catch (e: CategoryNotFoundException) {
            logger.warn("GET /api/v1/categories/{} - Category not found: {}", id, e.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(
                    code = "CATEGORY_NOT_FOUND",
                    message = e.message ?: "Category not found",
                    details = mapOf("categoryId" to e.categoryId)
                ))
        } catch (e: DatabaseOperationException) {
            logger.error("GET /api/v1/categories/{} - Database error: {}", id, e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(
                    code = "DATABASE_ERROR",
                    message = "Failed to fetch category. Please try again later.",
                    details = mapOf("operation" to e.operation)
                ))
        } catch (e: Exception) {
            logger.error("GET /api/v1/categories/{} - Unexpected error: {}", id, e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(
                    code = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred. Please try again later."
                ))
        }
    }
    
    @GetMapping("/active")
    fun getAllActive(): ResponseEntity<Any> {
        logger.info("GET /api/v1/categories/active - getAllActive")
        return try {
            val categories = getCategoryUseCase.findAllActive()
            ResponseEntity.status(HttpStatus.OK).body(categories)
        } catch (e: DatabaseOperationException) {
            logger.error("GET /api/v1/categories/active - Database error: {}", e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(
                    code = "DATABASE_ERROR",
                    message = "Failed to fetch active categories. Please try again later.",
                    details = mapOf("operation" to e.operation)
                ))
        } catch (e: Exception) {
            logger.error("GET /api/v1/categories/active - Unexpected error: {}", e.message, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(
                    code = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred. Please try again later."
                ))
        }
    }
    
    private fun createErrorResponse(
        code: String,
        message: String,
        details: Map<String, Any>? = null
    ): Map<String, Any> {
        return buildMap {
            put("code", code)
            put("message", message)
            details?.forEach { (key, value) ->
                put(key, value)
            }
        }
    }

}