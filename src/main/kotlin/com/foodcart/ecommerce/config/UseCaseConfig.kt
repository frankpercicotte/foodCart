package com.foodcart.ecommerce.config

import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import com.foodcart.ecommerce.core.domain.product.service.CategoryPricingService
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCase
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCaseImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class UseCaseConfig {
    
    @Bean
    @Primary
    fun createProductUseCase(
        productRepository: ProductRepository,
        categoryRepository: CategoryRepository,
        categoryPricingService: CategoryPricingService
    ): CreateProductUseCase = CreateProductUseCaseImpl(
        productRepository = productRepository,
        categoryRepository = categoryRepository,
        categoryPricingService = categoryPricingService
    )
}
