package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.shared.Result
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateCategoryUseCaseImpl(
    private val categoryRepository: CategoryRepository,
): CreateCategoryUseCase {
    companion object {
        private val logger = LoggerFactory.getLogger(CreateCategoryUseCaseImpl::class.java)
    }
    override fun execute(input: CreateCategoryUseCase.Input): Result<Category, ProductError> {

        if(categoryRepository.existsByName(input.name)){
            logger.error("Category name already exists name={}", input.name)
            return Result.Failure(ProductError.CategoryNameAlreadyExists(input.name))
        }

        val category = Category(
            categoryId = null,
            name = input.name,
            profitMargin = input.profitMargin,
            maxDiscount = input.maxDiscount,
            isActive = input.isActive,
        )
        val saveCategory = categoryRepository.save(category)
        logger.info("Category created id={} name={}", saveCategory.categoryId, saveCategory.name)
        return Result.Success(saveCategory)
    }
}