package org.takingroot.assignment.utils

import android.util.Patterns

class VerificationUtil {

    fun isEmailValid(email: String): ValidationResult {
        return if (email.isNotBlank() && email.contains('@')) {
            ValidationResult(Patterns.EMAIL_ADDRESS.matcher(email).matches())
        } else {
            ValidationResult(false)
        }
    }

    fun isFieldValid(username: String?): ValidationResult{
        return if (username.isNullOrEmpty()) {
            ValidationResult(false)
        } else {
            ValidationResult(username.isNotBlank())
        }
    }
}

data class ValidationResult(
    val status: Boolean = false,
)