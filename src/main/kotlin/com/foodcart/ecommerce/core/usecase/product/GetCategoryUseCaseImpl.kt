package com.foodcart.ecommerce.core.usecase.product

import com.foodcart.ecommerce.core.domain.common.exception.CategoryNotFoundException
import com.foodcart.ecommerce.core.domain.common.exception.DatabaseOperationException
import com.foodcart.ecommerce.core.domain.product.model.Category
import com.foodcart.ecommerce.core.domain.product.port.CategoryRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GetCategoryUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetCategoryUseCase {

    companion object {
        private val logger = LoggerFactory.getLogger(GetCategoryUseCaseImpl::class.java)
    }

    override fun findAll(): List<Category> {
        logger.info("Fetching all categories")
        return try {
            val categories = categoryRepository.findAll()
            logger.info("Found ${categories.size} categories")
            categories
        } catch (ex: Exception) {
            val errorMessage = "Error fetching categories"
            logger.error(errorMessage, ex)
            throw DatabaseOperationException("findAll()", errorMessage, ex)
        }
    }

    override fun findById(id: Long): Category {
        logger.info("Fetching category by id: $id")
        return try {
            val category = categoryRepository.findById(id)
            if (category != null) {
                category
            } else {
                val errorMessage = "Category not found with id: $id"
                logger.warn(errorMessage)
                throw CategoryNotFoundException(id, errorMessage)
            }
        } catch (ex: CategoryNotFoundException) {
            throw ex
        } catch (ex: Exception) {
            val errorMessage = "Error fetching category with id: $id"
            logger.error(errorMessage, ex)
            throw DatabaseOperationException("findById($id)", errorMessage, ex)
        }
    }

    override fun findAllActive(): List<Category> {
        logger.info("Fetching active categories")
        return try {
            val categories = categoryRepository.findAllActive()
            logger.info("Found ${categories.size} active categories")
            categories
        } catch (ex: Exception) {
            val errorMessage = "Error fetching active categories"
            logger.error(errorMessage, ex)
            throw DatabaseOperationException("findAllActive()", errorMessage, ex)
        }
    }
}
