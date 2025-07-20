package com.foodcart.ecommerce.core.shared.validation

import com.foodcart.ecommerce.core.shared.Result
import com.foodcart.ecommerce.core.shared.Result.Failure
import com.foodcart.ecommerce.core.shared.Result.Success

inline fun <E> validate(
    condition: Boolean,
    error: () -> E
): Result<Unit, E> =
    if (condition) Success(Unit) else Failure(error())

