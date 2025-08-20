package com.foodcart.ecommerce.core.testsupport.config

import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import com.foodcart.ecommerce.core.domain.product.service.CategoryPricingService
import com.foodcart.ecommerce.core.testsupport.inmemory.InMemoryCategoryRepository
import com.foodcart.ecommerce.core.testsupport.inmemory.InMemoryProductRepository
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCase
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCaseImpl
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@TestConfiguration
class ProductControllerWebMvcTestConfig {
    @Bean
    fun productRepository(): InMemoryProductRepository = InMemoryProductRepository()

    @Bean
    fun categoryRepository(): InMemoryCategoryRepository = InMemoryCategoryRepository()

    @Bean
    fun categoryPricingService(): CategoryPricingService = CategoryPricingService()

    @Bean
    fun createProductUseCase(
        productRepository: ProductRepository,
        categoryRepository: CategoryRepository,
        categoryPricingService: CategoryPricingService
    ): CreateProductUseCase = CreateProductUseCaseImpl(productRepository, categoryRepository, categoryPricingService)

    @Bean
    fun validator(): LocalValidatorFactoryBean = LocalValidatorFactoryBean()
}