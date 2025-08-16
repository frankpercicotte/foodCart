package com.foodcart.ecommerce.adapters.inbound.http.product.controller

import com.foodcart.ecommerce.adapters.inbound.http.common.dto.ErrorResponse
import com.foodcart.ecommerce.adapters.inbound.http.common.mapper.toHttp
import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateProductRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toCreateResponse
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toInput
import com.foodcart.ecommerce.core.shared.Result
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

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val createProductUseCase: CreateProductUseCase
) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<Any> {
        val input = request.toInput()
        return when (val result = createProductUseCase.execute(input)) {
            is Result.Success -> {
                val product = result.value
                val body = product.toCreateResponse()
                ResponseEntity.status(HttpStatus.CREATED).body(body)
            }
            is Result.Failure -> {
                val (status, errorBody) = (result.error).toHttp()
                ResponseEntity.status(status).body(errorBody)
            }
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<Any> {
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
        // Soft delete expected: set isActive = false
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            ErrorResponse(
                code = "NOT_IMPLEMENTED",
                message = "DELETE /api/v1/products/{id} (soft delete) not implemented yet",
                context = mapOf("id" to id)
            )
        )
    }
}