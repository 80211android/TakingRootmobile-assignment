package org.takingroot.assignment.domain

/**
 * Created by Saurabh
 */
sealed class UIEvent {
    data class AccountChanged(val account: String): UIEvent()
    data class ConfirmAccountChanged(val confirmAccount: String): UIEvent()
    data class CodeChanged(val code: String): UIEvent()
    data class NameChanged(val name: String): UIEvent()
    data class LastNameChanged(val name: String): UIEvent()

    data class EmailChanged(val email: String): UIEvent()

    object Submit: UIEvent()
}
