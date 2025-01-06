package com.crezent.finalyearproject.domain.util

sealed interface ValidationError : Error {

    data object EmptyField : ValidationError

    data object WeakField : ValidationError

    data object EmailError : ValidationError

    data object NumberError : ValidationError

}

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        return emailRegex.matches(email)

    }

    fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = Regex("^\\+?[1-9]\\d{1,14}$") // E.164 format
        return phoneRegex.matches(phone)
    }

    fun isPasswordStrong(password: String): Boolean {
        val passwordRegex = "(?=.*[A-Z])(?=.*[0-9])(?=.*\\W)(?=\\S+$).{8,}$".toRegex()

        return passwordRegex.matches(password)
    }

    fun passwordContainsCapital(
        password: String
    ): Boolean {
        return password.any {
            it.isUpperCase()
        }
    }

    fun passwordContainsDigit(
        password: String
    ): Boolean {

        return password.any {
            it.isDigit()
        }
    }

    fun fieldContainsSpace(
        password: String
    ): Boolean {
        return password.any {
            it.isWhitespace()
        }
    }

    fun fieldContainsSpecialCharacter(
        password: String
    ): Boolean {
        return password.any {
            !it.isLetterOrDigit() && !it.isWhitespace()
        }
    }


}
