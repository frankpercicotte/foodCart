package com.foodcart.ecommerce.core.adapter.http.product.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.foodcart.ecommerce.adapters.inbound.http.product.controller.ProductController
import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateProductRequest
import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.testsupport.inmemory.InMemoryCategoryRepository
import com.foodcart.ecommerce.core.testsupport.inmemory.InMemoryProductRepository
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@WebMvcTest(controllers = [ProductController::class])
@Import(com.foodcart.ecommerce.core.testsupport.config.ProductControllerWebMvcTestConfig::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var productRepository: InMemoryProductRepository

    @Autowired
    private lateinit var categoryRepository: InMemoryCategoryRepository

    @BeforeEach
    fun setup() {
        productRepository.reset()
        categoryRepository.reset()
        categoryRepository.save(
            Category(
                1L,
                "Default",
                BigDecimal("20"),
                BigDecimal("50"),
                true
            )
        )
    }

    private fun validCreateRequest(): CreateProductRequest =
        CreateProductRequest(
            name = "Quadro Mona Lisa",
            description = "Quadro Decorativo Mona Lisa 77x53",
            cost = BigDecimal("200.00"),
            discount = BigDecimal("0.00"),
            categoryId = "1",
            stockQuantity = 10,
            imageUrl = "http://quadro-decorativo-monalisa77x53.webp"
        )

    @Test
    fun `should return 201 and response body when product is created successfully`() {
        val request = validCreateRequest()

        mockMvc.perform(
            post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", equalTo("Quadro Mona Lisa")))
            .andExpect(jsonPath("$.description", equalTo("Quadro Decorativo Mona Lisa 77x53")))
            .andExpect(jsonPath("$.price", equalTo(240.00)))
            .andExpect(jsonPath("$.cost", equalTo(200.00)))
            .andExpect(jsonPath("$.discount", equalTo(0.00)))
            .andExpect(jsonPath("$.categoryId", equalTo("1")))
            .andExpect(jsonPath("$.stockQuantity", equalTo(10)))
            .andExpect(jsonPath("$.isActive", equalTo(true)))
            .andExpect(jsonPath("$.imageUrl", equalTo("http://quadro-decorativo-monalisa77x53.webp")))
    }

    @Test
    fun `should return 409 when product name already exists`() {
        val request = validCreateRequest()
        productRepository.save(
            Product(
                productId = "999",
                name = request.name,
                normalizedName = request.name.lowercase(),
                description = request.description,
                price = BigDecimal("10.00"),
                cost = request.cost,
                discount = request.discount,
                categoryId = request.categoryId,
                stockQuantity = request.stockQuantity,
                isActive = true,
                imageUrl = request.imageUrl
            )
        )

        mockMvc.perform(
            post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isConflict)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", equalTo(ProductError.ProductNameAlreadyExists(request.name).code)))
            .andExpect(jsonPath("$.message", equalTo(ProductError.ProductNameAlreadyExists(request.name).message)))
            .andExpect(jsonPath("$.context.name", equalTo(request.name)))
    }

    @Test
    fun `should return 400 when validation fails on create`() {
        val invalidRequest = CreateProductRequest(
            name = "",
            description = "",
            cost = BigDecimal("-1.00"),
            discount = BigDecimal("-0.10"),
            categoryId = "",
            stockQuantity = -1,
            imageUrl = null
        )

        mockMvc.perform(
            post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 501 with error body for GET by id`() {
        val id = "42"

        mockMvc.perform(get("/api/v1/products/{id}", id))
            .andExpect(status().isNotImplemented)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", equalTo("NOT_IMPLEMENTED")))
            .andExpect(jsonPath("$.message", equalTo("GET /api/v1/products/{id} not implemented yet")))
            .andExpect(jsonPath("$.context.id", equalTo(id)))
    }

    @Test
    fun `should return 501 with error body for DELETE by id`() {
        val id = "77"

        mockMvc.perform(delete("/api/v1/products/{id}", id))
            .andExpect(status().isNotImplemented)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", equalTo("NOT_IMPLEMENTED")))
            .andExpect(jsonPath("$.message", equalTo("DELETE /api/v1/products/{id} (soft delete) not implemented yet")))
            .andExpect(jsonPath("$.context.id", equalTo(id)))
    }
}
