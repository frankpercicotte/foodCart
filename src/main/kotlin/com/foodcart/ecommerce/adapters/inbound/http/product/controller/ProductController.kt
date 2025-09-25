package com.foodcart.ecommerce.adapters.inbound.http.product.controller

import com.foodcart.ecommerce.adapters.inbound.http.common.dto.ErrorResponse
import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateProductRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toCreateResponse
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toInput
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCase
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
@RequestMapping("/api/v1/products")
class ProductController(
    private val createProductUseCase: CreateProductUseCase
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ProductController::class.java)
    }

    @PostMapping
    fun create(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<Any> {
        logger.info("POST /api/v1/products - Received request: name={}, categoryId={}", request.name, request.categoryId)
        val input = request.toInput()
        val product = createProductUseCase.execute(input)
        val body = product.toCreateResponse()
        logger.info("POST /api/v1/products - Success: productId={}, name={}", product.productId, product.name)
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @GetMapping()
    fun getAll(): ResponseEntity<Any> {
        logger.info("GET /api/v1/products - getAll - NOT_IMPLEMENTED.")
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            ErrorResponse(
                code = "NOT_IMPLEMENTED",
                message = "GET /api/v1/products/{id} not implemented yet",
                context = mapOf("Not implemented" to "Sorry, we're still working on it.")
            )
        )
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<Any> {
        logger.info("GET /api/v1/products - getById - NOT_IMPLEMENTED.")
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            ErrorResponse(
                code = "NOT_IMPLEMENTED",
                message = "GET /api/v1/products/{id} not implemented yet",
                context = mapOf("id" to id)
            )
        )
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @Valid @RequestBody request: CreateProductRequest): ResponseEntity<Any> {
        logger.info("PUT /api/v1/products - NOT_IMPLEMENTED.")
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            ErrorResponse(
                code = "NOT_IMPLEMENTED",
                message = "PUT /api/v1/products/{id} not implemented yet",
                context = mapOf("id" to id)
            )
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Any> {
        logger.info("DELETE /api/v1/products - NOT_IMPLEMENTED.")
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            ErrorResponse(
                code = "NOT_IMPLEMENTED",
                message = "DELETE /api/v1/products/{id} (soft delete) not implemented yet",
                context = mapOf("id" to id)
            )
        )
    }
}