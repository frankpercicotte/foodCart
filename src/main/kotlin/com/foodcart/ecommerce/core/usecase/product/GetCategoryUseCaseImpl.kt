package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.ProductError
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import com.foodcart.ecommerce.core.shared.Result
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GetCategoryUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetCategoryUseCase {

    companion object {
        private val logger = LoggerFactory.getLogger(GetCategoryUseCaseImpl::class.java)
    }

    override fun findAll(): Result<List<Category>, ProductError> {
        logger.info("Fetching all categories")
        return try {
            val categories = categoryRepository.findAll()
            logger.info("Found ${categories.size} categories")
            Result.Success(categories)
        } catch (ex: Exception) {
            logger.error("Error fetching categories", ex)
            Result.Failure(ProductError.DatabaseError("findAll()", "Failed to fetch categories"))
        }
    }

    override fun findById(id: Long): Result<Category, ProductError> {
        logger.info("Fetching category by id: $id")
        return try {
            val category = categoryRepository.findById(id)
            if (category != null) {
                Result.Success(category)
            } else {
                logger.warn("Category not found with id: $id")
                Result.Failure(ProductError.CategoryNotFound(id))
            }
        } catch (ex: Exception) {
            logger.error("Error fetching category with id: $id", ex)
            Result.Failure(ProductError.DatabaseError("findById($id)", "Failed to fetch category"))
        }
    }

    override fun findAllActive(): Result<List<Category>, ProductError> {
        logger.info("Fetching active categories")
        return try {
            val categories = categoryRepository.findAllActive()
            logger.info("Found ${categories.size} active categories")
            Result.Success(categories)
        } catch (ex: Exception) {
            logger.error("Error fetching active categories", ex)
            Result.Failure(ProductError.DatabaseError("findAllActive()", "Failed to fetch active categories"))
        }
    }
}
