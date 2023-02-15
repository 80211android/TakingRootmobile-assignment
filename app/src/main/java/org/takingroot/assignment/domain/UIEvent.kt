package org.takingroot.assignment.domain


sealed class UIEvent {
    data class NameChanged(val name: String): UIEvent()
    data class LastNameChanged(val name: String): UIEvent()
    data class EmailChanged(val email: String): UIEvent()
    data class BirthDateChanged(val date: String): UIEvent()

    object Submit: UIEvent()
}
