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

    data object TokenError : DatabaseErrorToType(
        duplicateEntry = "Token already exist",
        insertingError = "Unable to add token ",
        itemNotFound = "Incorrect Otp"
    )

    data object ResetPasswordError : DatabaseErrorToType(
        itemNotFound = "This email is not associated with any account"
    )

    data object CardError : DatabaseErrorToType(
        duplicateEntry = "This card is already associated with an account",
        insertingError = "Unable to add card ",
        itemNotFound = "No card was found "
    )

    data object TransactionError : DatabaseErrorToType(
        duplicateEntry = "This transaction with this account",
        insertingError = "Unable to add transaction ",
        itemNotFound = "No transaction with this Id exist "
    )


}