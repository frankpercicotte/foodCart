package com.foodcart.ecommerce

import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.domain.product.port.ProductRepository
import com.foodcart.ecommerce.core.domain.product.service.CategoryPricingService
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCase
import com.foodcart.ecommerce.core.usecase.product.CreateProductUseCaseImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("h2")
class H2BeansConfig {

    @Bean
    fun categoryPricingService(): CategoryPricingService = CategoryPricingService()
}