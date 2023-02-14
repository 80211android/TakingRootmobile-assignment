package org.takingroot.assignment.domain

/**
 * Created by Saurabh
 */
data class UIState(
    val username: String = "",
    val userLastname: String = "",

    val email: String = "",
    val birthDate: String = "",


    val confirmAccountNumber: String = "",
    val code: String = "",
    val ownerName: String = "",
    val usernameError: Boolean = false,
    val userLastnameError: Boolean = false,
    val emailError: Boolean = false,
    val birthDateError: Boolean = false,
    val hasNameError: Boolean = false,
)