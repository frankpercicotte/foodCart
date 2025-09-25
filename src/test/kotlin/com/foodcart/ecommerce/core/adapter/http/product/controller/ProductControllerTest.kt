package com.foodcart.ecommerce.core.adapter.http.product.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.foodcart.ecommerce.adapters.inbound.http.product.controller.ProductController
import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateProductRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toInput
import com.foodcart.ecommerce.adapters.inbound.http.exception.GlobalExceptionHandler
import com.foodcart.ecommerce.core.domain.common.exception.CategoryNotFoundException
import com.foodcart.ecommerce.core.domain.common.exception.ProductNameAlreadyExistsException
import com.foodcart.ecommerce.core.domain.product.model.Product
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCase
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal
import org.mockito.BDDMockito.given

@WebMvcTest(controllers = [ProductController::class])
@Import(GlobalExceptionHandler::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var createProductUseCase: CreateProductUseCase

    @BeforeEach
    fun setup() {
        // No-op: usando mock do use case
    }

    private fun validCreateRequest(): CreateProductRequest =
        CreateProductRequest(
            name = "Quadro Mona Lisa",
            description = "Quadro Decorativo Mona Lisa 77x53",
            cost = BigDecimal("200.00"),
            discount = BigDecimal("0.00"),
            categoryId = 1L,
            stockQuantity = 10,
            imageUrl = "http://quadro-decorativo-monalisa77x53.webp"
        )

    @Test
    fun `should return 201 and response body when product is created successfully`() {
        val request = validCreateRequest()

        val product = Product(
            productId = 1L,
            name = request.name,
            normalizedName = request.name.lowercase(),
            description = request.description,
            price = BigDecimal("240.00"),
            cost = request.cost,
            discount = request.discount,
            categoryId = request.categoryId,
            stockQuantity = request.stockQuantity,
            isActive = true,
            imageUrl = request.imageUrl
        )

        given(createProductUseCase.execute(request.toInput())).willReturn(product)

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
            .andExpect(jsonPath("$.categoryId", equalTo(1)))
            .andExpect(jsonPath("$.stockQuantity", equalTo(10)))
            .andExpect(jsonPath("$.isActive", equalTo(true)))
            .andExpect(jsonPath("$.imageUrl", equalTo("http://quadro-decorativo-monalisa77x53.webp")))
    }

    @Test
    fun `should return 409 when product name already exists`() {
        val request = validCreateRequest()
        given(createProductUseCase.execute(request.toInput())).willAnswer { throw ProductNameAlreadyExistsException(request.name) }

        mockMvc.perform(
            post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isConflict)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", equalTo("PRODUCT_NAME_ALREADY_EXISTS")))
            .andExpect(jsonPath("$.message").isString)
            .andExpect(jsonPath("$.name", equalTo(request.name)))
    }

    @Test
    fun `should return 400 when validation fails on create`() {
        val invalidRequest = CreateProductRequest(
            name = "",
            description = "",
            cost = BigDecimal("-1.00"),
            discount = BigDecimal("-0.10"),
            categoryId = 1L,
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

}
