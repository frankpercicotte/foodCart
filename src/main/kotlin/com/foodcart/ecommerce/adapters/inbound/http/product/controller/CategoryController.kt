package com.foodcart.ecommerce.adapters.inbound.http.product.controller

import com.foodcart.ecommerce.adapters.inbound.http.common.dto.ErrorResponse
import com.foodcart.ecommerce.adapters.inbound.http.common.mapper.toHttp
import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateCategoryRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateProductRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toCreateResponse
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toInput
import com.foodcart.ecommerce.core.shared.Result
import com.foodcart.ecommerce.core.usecase.product.CreateCategoryUseCase
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCase
import com.foodcart.ecommerce.core.usecase.product.GetCategoryUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.slf4j.LoggerFactory

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
        logger.info("POST /api/v1/categories - Received request: {}, {}, {}, {}",
            request.name, request.profitMargin, request.maxDiscount, request.isActive
        )
        val input = request.toInput()
        return when (val result = createCategoryUseCase.execute(input)) {
            is Result.Success -> {
                val category = result.value
                val body = category.toCreateResponse()
                logger.info("POST /api/v1/categories - Success: categoryId={}, name={}", category.categoryId, category.name)
                ResponseEntity.status(HttpStatus.CREATED).body(body)
            }
            is Result.Failure -> {
                val (status, errorBody) = (result.error).toHttp()
                logger.warn(
                    "POST /api/v1/categories - Domain error: code={} message={} context={}",
                    result.error.code,
                    result.error.message,
                    result.error.context
                )
                ResponseEntity.status(status).body(errorBody)
            }
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<Any> {
        logger.info("GET /api/v1/categories - getAll")
        return when (val result = getCategoryUseCase.findAll()) {
            is Result.Success -> {
                ResponseEntity.status(HttpStatus.OK).body(result.value)
            }
            is Result.Failure -> {
                logger.warn(
                    "Get /api/v1/categories - DatabaseError: {} - {} - {}",
                    result.error.code,
                    result.error.message,
                    result.error.context
                )
                val (status, errorBody) = result.error.toHttp()
                ResponseEntity.status(status).body(errorBody)
            }
        }
    }

}