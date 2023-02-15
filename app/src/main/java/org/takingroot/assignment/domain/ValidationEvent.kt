package org.takingroot.assignment.domain

sealed class ValidationEvent {
    object Success: ValidationEvent()
}
