package com.foodcart.ecommerce.adapters.inbound.http.product.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.foodcart.ecommerce.adapters.inbound.http.exception.GlobalExceptionHandler
import com.foodcart.ecommerce.adapters.inbound.http.product.dto.CreateCategoryRequest
import com.foodcart.ecommerce.adapters.inbound.http.product.mapper.toInput
import com.foodcart.ecommerce.core.domain.common.exception.CategoryAlreadyExistsException
import com.foodcart.ecommerce.core.domain.common.exception.CategoryNotFoundException
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.usecase.product.CreateCategoryUseCase
import com.foodcart.ecommerce.core.usecase.product.GetCategoryUseCase
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@WebMvcTest(controllers = [CategoryController::class])
@Import(GlobalExceptionHandler::class)
class CategoryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var createCategoryUseCase: CreateCategoryUseCase

    @MockitoBean
    private lateinit var getCategoryUseCase: GetCategoryUseCase

    @Test
    fun `create should return 201 when category is created successfully`() {
        // Arrange
        val request = CreateCategoryRequest(
            name = "Electronics",
            profitMargin = BigDecimal("30.0"),
            maxDiscount = BigDecimal("15.0"),
            isActive = true
        )

        val created = Category(
            categoryId = 1L,
            name = request.name,
            profitMargin = request.profitMargin,
            maxDiscount = request.maxDiscount,
            isActive = request.isActive
        )

        given(createCategoryUseCase.execute(request.toInput())).willReturn(created)

        // Act & Assert
        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.categoryId").value(1))
            .andExpect(jsonPath("$.name").value("Electronics"))
            .andExpect(jsonPath("$.profitMargin").value(30.0))
            .andExpect(jsonPath("$.maxDiscount").value(15.0))
            .andExpect(jsonPath("$.isActive").value(true))
    }

    @Test
    fun `create should return 409 when category already exists`() {
        // Arrange
        val request = CreateCategoryRequest(
            name = "Electronics",
            profitMargin = BigDecimal("30.0"),
            maxDiscount = BigDecimal("15.0"),
            isActive = true
        )

        given(createCategoryUseCase.execute(request.toInput())).willAnswer { throw CategoryAlreadyExistsException(request.name) }

        // Act & Assert (uma única chamada já deve resultar em 409)
        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value("CATEGORY_ALREADY_EXISTS"))
            .andExpect(jsonPath("$.message").isString)
            .andExpect(jsonPath("$.name").value("Electronics"))
    }

    @Test
    fun `getAll should return 200 with categories`() {
        // Arrange
        val categories = listOf(
            Category(
                categoryId = 1L,
                name = "Electronics",
                profitMargin = BigDecimal("30.0"),
                maxDiscount = BigDecimal("15.0"),
                isActive = true
            ),
            Category(
                categoryId = 2L,
                name = "Weapons",
                profitMargin = BigDecimal("40.0"),
                maxDiscount = BigDecimal("20.0"),
                isActive = true
            )
        )

        given(getCategoryUseCase.findAll()).willReturn(categories)

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").isString)
            .andExpect(jsonPath("$[1].name").isString)
    }

    @Test
    fun `getById should return 200 with category`() {
        // Arrange
        val savedCategory = Category(
            categoryId = 10L,
            name = "Electronics",
            profitMargin = BigDecimal("30.0"),
            maxDiscount = BigDecimal("15.0"),
            isActive = true
        )

        given(getCategoryUseCase.findById(10L)).willReturn(savedCategory)

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/${savedCategory.categoryId}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.categoryId").value(savedCategory.categoryId))
            .andExpect(jsonPath("$.name").value("Electronics"))
            .andExpect(jsonPath("$.profitMargin").value(30.0))
            .andExpect(jsonPath("$.maxDiscount").value(15.0))
            .andExpect(jsonPath("$.isActive").value(true))
    }

    @Test
    fun `getById should return 404 when category not found`() {
        // Arrange
        given(getCategoryUseCase.findById(999L)).willAnswer { throw CategoryNotFoundException(999) }

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/999"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value("CATEGORY_NOT_FOUND"))
            .andExpect(jsonPath("$.message").isString)
            .andExpect(jsonPath("$.categoryId").value(999))
    }

    @Test
    fun `getAllActive should return 200 with active categories`() {
        // Arrange
        given(getCategoryUseCase.findAllActive()).willReturn(
            listOf(
                Category(1L, "Electronics", BigDecimal("30.0"), BigDecimal("15.0"), true),
                Category(2L, "Weapons", BigDecimal("40.0"), BigDecimal("20.0"), true)
            )
        )

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/active"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].isActive").value(true))
            .andExpect(jsonPath("$[1].isActive").value(true))
    }

    @Test
    fun `create should return 400 when request is invalid`() {
        // Arrange
        val request = CreateCategoryRequest(
            name = "",
            profitMargin = BigDecimal("-10.0"),
            maxDiscount = BigDecimal("150.0"),
            isActive = true
        )

        // Act & Assert
        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Validation Error"))
            .andExpect(jsonPath("$.message").value("Validation failed for request"))
            .andExpect(jsonPath("$.errors").isArray)
            .andExpect(jsonPath("$.errors[?(@.field == 'name')].message").value("Name is required"))
            .andExpect(jsonPath("$.errors[?(@.field == 'profitMargin')].message").value("must be greater than or equal to 0.00"))
            .andExpect(jsonPath("$.errors.length()").value(2))
    }
}
