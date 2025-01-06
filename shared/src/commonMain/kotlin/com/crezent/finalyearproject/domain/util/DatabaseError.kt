package com.crezent.finalyearproject.domain.util

sealed interface DatabaseError : Error {
    data object InsertingError : DatabaseError

    data object ItemNotFound : DatabaseError

    data object DuplicateEntry : DatabaseError

    data class OtherError(val error: String) : DatabaseError
}

fun DatabaseError.toErrorMessage(): String {
    return when (this) {
        DatabaseError.DuplicateEntry -> "Item already exist"
        DatabaseError.InsertingError -> "Unable to insert item"
        DatabaseError.ItemNotFound -> "Item with this not found"
        is DatabaseError.OtherError -> error
    }
}