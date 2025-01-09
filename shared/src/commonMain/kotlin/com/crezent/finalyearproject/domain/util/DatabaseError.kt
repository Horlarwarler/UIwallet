package com.crezent.finalyearproject.domain.util

sealed interface DatabaseError : Error {
    data object InsertingError : DatabaseError

    data object ItemNotFound : DatabaseError

    data object DuplicateEntry : DatabaseError

    data class OtherError(val error: String) : DatabaseError
}

fun DatabaseError.toErrorMessage(
    errorToType: DatabaseErrorToType = DatabaseErrorToType.UserError
): String {
    return when (this) {
        DatabaseError.DuplicateEntry -> errorToType.duplicateEntry
        DatabaseError.InsertingError -> errorToType.insertingError
        DatabaseError.ItemNotFound -> errorToType.itemNotFound
        is DatabaseError.OtherError -> error
    }
}

sealed class DatabaseErrorToType(
    val duplicateEntry: String = "Item already exist",
    val insertingError: String = "Unable to insert item",
    val itemNotFound: String = "Item with this not found"
) {
    data object UserError : DatabaseErrorToType(
        duplicateEntry = "User With this email, matric number or phone number already exist",
        insertingError = "Unable to register user",
        itemNotFound = "email address or phone number not correct"
    )
}