package com.foodcart.ecommerce.core.domain.common

sealed class Result<T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure<T>(val error: DomainError) : Result<T>()

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure

    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(value)
        return this
    }

    inline fun onFailure(action: (DomainError) -> Unit): Result<T> {
        if (this is Failure) action(error)
        return this
    }

    inline fun <R> map(transform: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(value))
            is Failure -> Failure(error)
        }
    }

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun getOrDefault(default: T): T = when (this) {
        is Success -> value
        is Failure -> default
    }
}
