package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.exception.CategoryAlreadyExistsException
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateCategoryUseCaseImpl(
    private val categoryRepository: CategoryRepository,
) : CreateCategoryUseCase {

    companion object {
        private val logger = LoggerFactory.getLogger(CreateCategoryUseCaseImpl::class.java)
    }

    override fun execute(input: CreateCategoryUseCase.Input): Category {
        if (categoryRepository.existsByName(input.name)) {
            logger.error("Category name already exists name={}", input.name)
            throw CategoryAlreadyExistsException(input.name)
        }

        val category = Category(
            categoryId = null,
            name = input.name,
            profitMargin = input.profitMargin,
            maxDiscount = input.maxDiscount,
            isActive = input.isActive,
        )
        
        val savedCategory = categoryRepository.save(category)
        logger.info("Category created id={} name={}", savedCategory.categoryId, savedCategory.name)
        return savedCategory
    }
}