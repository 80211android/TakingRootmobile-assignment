package org.takingroot.assignment.domain

/**
 * Created by Saurabh
 */
sealed class ValidationEvent {
    object Success: ValidationEvent()
}
