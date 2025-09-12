package com.foodcart.ecommerce.adapters.inbound.http.product.controller

import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateCategoryRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toCreateResponse
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toInput
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
        val category = createCategoryUseCase.execute(request.toInput())
        val body = category.toCreateResponse()
        logger.info("POST /api/v1/categories - Success: categoryId={}, name={}", category.categoryId, category.name)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @GetMapping
    fun getAll(): ResponseEntity<Any> {
        logger.info("GET /api/v1/categories - getAll")
        val categories = getCategoryUseCase.findAll()
        return ResponseEntity.status(HttpStatus.OK).body(categories)
    }
    
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Any> {
        logger.info("GET /api/v1/categories/{} - getById", id)
        val category = getCategoryUseCase.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(category)
    }
    
    @GetMapping("/active")
    fun getAllActive(): ResponseEntity<Any> {
        logger.info("GET /api/v1/categories/active - getAllActive")
        val categories = getCategoryUseCase.findAllActive()
        return ResponseEntity.status(HttpStatus.OK).body(categories)
    }

}