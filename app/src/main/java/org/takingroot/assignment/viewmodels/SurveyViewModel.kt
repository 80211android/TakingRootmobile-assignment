package org.takingroot.assignment.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.takingroot.assignment.domain.UIEvent
import org.takingroot.assignment.domain.UIState
import org.takingroot.assignment.domain.ValidationEvent
import org.takingroot.assignment.domain.Validator
import org.takingroot.assignment.models.AppDatabase
import org.takingroot.assignment.models.Survey
import org.takingroot.assignment.networking.BaseAPIService
import org.takingroot.assignment.networking.RetrofitInstance
import org.takingroot.assignment.repositories.ISurveyRepository
import org.takingroot.assignment.repositories.SurveyRepository
import org.takingroot.assignment.utils.VerificationUtil

class SurveyViewModel(
    private val repository: ISurveyRepository,
    private val apiService: BaseAPIService,
    private val verificationUtil: VerificationUtil
) : ViewModel() {
    val surveys = repository.surveys

    private var _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    val validationEvent = MutableSharedFlow<ValidationEvent>()

    init {
        this.refresh()
    }

    fun send(vararg surveys: Survey) = viewModelScope.launch(Dispatchers.IO) {
//        withContext(this.coroutineContext) {
//            surveys.forEach {
//                val r = apiService.response("user", it)
//                val t = r
//
//                repository.delete(it)
//            }
//            repository.fetchAll()
//        }

        surveys.forEach {
            val response = apiService.response("user", it)
            val t = response

            repository.delete(it)
        }
        repository.fetchAll()
    }

    fun sendSurvey(survey: Survey) = viewModelScope.launch(Dispatchers.IO) {
        val r = apiService.response("user", survey)
        val t = r
        repository.fetchAll()

    }

    fun save(vararg surveys: Survey) = viewModelScope.launch(Dispatchers.IO) {
        withContext(this.coroutineContext) {
            repository.save(
                *surveys
            )
        }
        refresh()
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        withContext(this.coroutineContext) { repository.fetchAll() }
    }

    fun onEvent(event: UIEvent) {
        when(event) {
            is UIEvent.AccountChanged -> {
                _uiState.value = _uiState.value.copy(
                    userLastname = event.account
                )
            }
            is UIEvent.ConfirmAccountChanged -> {
                _uiState.value = _uiState.value.copy(
                    confirmAccountNumber = event.confirmAccount
                )
            }
            is UIEvent.CodeChanged -> {
                _uiState.value = _uiState.value.copy(
                    code = event.code
                )
            }
            is UIEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(
                    username = event.name
                )
                _uiState.value = _uiState.value.copy(
                    usernameError = verificationUtil.isFieldValid(_uiState.value.username).status.not()
                )
            }
            is UIEvent.LastNameChanged -> {
                _uiState.value = _uiState.value.copy(
                    userLastname = event.name
                )
                _uiState.value = _uiState.value.copy(
                    userLastnameError = verificationUtil.isFieldValid(_uiState.value.userLastname).status.not()
                )
            }
            is UIEvent.EmailChanged-> {
                _uiState.value = _uiState.value.copy(
                    email = event.email
                )
                _uiState.value = _uiState.value.copy(
                    emailError = verificationUtil.isEmailValid(_uiState.value.email).status.not()
                )
            }
            is UIEvent.BirthDateChanged -> {
                _uiState.value = _uiState.value.copy(
                    birthDate = event.date
                )
                _uiState.value = _uiState.value.copy(
                    birthDateError = verificationUtil.isBirthDateValid(_uiState.value.birthDate).status.not()
                )
            }
            is UIEvent.Submit -> {
                validateInputs()
            }
        }
    }

    private fun validateInputs() {
        val accountResult = Validator.validateAccountNumber(_uiState.value.userLastname)
        val confirmAccountResult = Validator.validateConfirmAccountNumber(
            _uiState.value.userLastname,
            _uiState.value.confirmAccountNumber
        )
        val codeResult = Validator.validateBankCode(_uiState.value.code)
        val nameResult = Validator.validateOwnerName(_uiState.value.ownerName)

        val usernameValid = verificationUtil.isFieldValid(_uiState.value.username).status
        val userLastnameValid = verificationUtil.isFieldValid(_uiState.value.userLastname).status
        val emailValid =  verificationUtil.isEmailValid(_uiState.value.email).status
        val birthDateValid = verificationUtil.isBirthDateValid(_uiState.value.birthDate).status

        _uiState.value = _uiState.value.copy(
            usernameError = usernameValid.not(),
            userLastnameError = userLastnameValid.not(),
            emailError = emailValid.not(),
            hasNameError = !nameResult.status,
            birthDateError = birthDateValid.not()
        )
        val hasError = listOf(
            accountResult,
            confirmAccountResult,
            codeResult,
            nameResult
        ).any { !it.status }


        viewModelScope.launch {
            if (usernameValid && userLastnameValid && emailValid) {

                val survey = Survey(
                    first_name = _uiState.value.username,
                    last_name = _uiState.value.userLastname,
                    birth_date = _uiState.value.birthDate,
                    email = uiState.value.email
//                    payload = mapOf(
//                        "Lastname" to _uiState.value.userLastname,
//                        "Email" to _uiState.value.email
//                    )
                )
                save(survey)
                validationEvent.emit(ValidationEvent.Success)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])

                val db = AppDatabase.getDatabase(application)
                val repository = SurveyRepository(db.surveyDao())
                val verificationUtil = VerificationUtil()
                return SurveyViewModel(repository, RetrofitInstance.getInstance(), verificationUtil) as T
            }
        }
    }
}