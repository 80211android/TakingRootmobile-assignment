package org.takingroot.assignment.utils

import android.util.Patterns
import java.time.LocalDate
import java.time.Period

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

    fun isBirthDateValid(date: String?): ValidationResult{
        return if (date.isNullOrEmpty()) {
            ValidationResult(false)
        } else {

            val dateList = date.split("-") ?: emptyList()
            if (dateList.size > 2) {
                val userAge = getAge(dateList[0], dateList[1], dateList[2])
                if (userAge >= 18) {
                    return ValidationResult(true)
                }
            }
            ValidationResult(false)
        }
    }

    private fun getAge(year: String, month: String, dayOfMonth: String): Int {
        return Period.between(
            LocalDate.of(year.toInt(), month.toInt(), dayOfMonth.toInt()),
            LocalDate.now()
        ).years
    }
}

data class ValidationResult(
    val status: Boolean = false,
)