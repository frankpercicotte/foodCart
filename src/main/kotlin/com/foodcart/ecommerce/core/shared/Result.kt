package com.foodcart.ecommerce.core.shared


sealed class Result<T, E> {
    data class Success<T, E>(val value: T) : Result<T, E>()
    data class Failure<T, E>(val error: E) : Result<T, E>()

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure

    inline fun onSuccess(action: (T) -> Unit): Result<T, E> {
        if (this is Success) action(value)
        return this
    }

    inline fun onFailure(action: (action: (E)) -> Unit): Result<T, E> {
        if (this is Failure) action(error)
        return this
    }

    inline fun <R> map(transform: (T) -> R): Result<R, E> {
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

    inline fun <R> flatMap(transform: (T) -> Result<R, E>): Result<R, E> {
        return when (this) {
            is Success -> transform(value)
            is Failure -> Failure(error)
        }
    }
}