package com.crezent.finalyearproject.domain.util

typealias ResultError = Error

sealed interface Result<out T, out ResultError> {

    data class Error<out ResultError>(val error: ResultError) : Result<Nothing, ResultError>

    data class Success<out T>(val data: T) : Result<T, Nothing>

}


inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {


    return when (this) {
        is Result.Success -> {
            Result.Success(map(data))
        }

        is Result.Error -> {
            Result.Error(error)
        }
    }
}


fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }

        is Result.Success -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>