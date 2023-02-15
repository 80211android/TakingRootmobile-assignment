package org.takingroot.assignment.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.takingroot.assignment.domain.UIEvent
import org.takingroot.assignment.domain.UIState
import org.takingroot.assignment.domain.ValidationEvent
import org.takingroot.assignment.models.Survey
import org.takingroot.assignment.repositories.RemoteRepository
import org.takingroot.assignment.repositories.ISurveyRepository
import org.takingroot.assignment.utils.VerificationUtil
import org.takingroot.assignment.viewmodels.map.SurveyMapper
import retrofit2.HttpException

class SurveyViewModel(
    private val repository: ISurveyRepository,
    private val remoteRepository: RemoteRepository,
    private val verificationUtil: VerificationUtil,
    private val mapper: SurveyMapper
) : ViewModel() {
    val surveys = repository.surveys

    private var _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState
    val validationEvent = MutableSharedFlow<ValidationEvent>()

    init {
        this.refresh()
    }

    fun send(vararg surveys: Survey) = viewModelScope.launch(Dispatchers.IO) {
        surveys.forEach { survey ->

            try {
                val response = remoteRepository.sendSurvey(survey)
                val surveyResponse = mapper.mapToSurvey(response)
                repository.delete(surveyResponse)

            } catch (exception: Exception) {

                when (exception) {
                    is HttpException -> {
                        val errorMessage = exception.response()?.errorBody()?.string()
                    }
                }
            }
        }
        repository.fetchAll()
    }

    fun save(vararg surveys: Survey) = viewModelScope.launch(Dispatchers.IO) {

        repository.save(*surveys)
        refresh()
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        withContext(this.coroutineContext) { repository.fetchAll() }
    }

    fun onEvent(event: UIEvent) {
        when(event) {
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
        val usernameValid = verificationUtil.isFieldValid(_uiState.value.username).status
        val userLastnameValid = verificationUtil.isFieldValid(_uiState.value.userLastname).status
        val emailValid =  verificationUtil.isEmailValid(_uiState.value.email).status
        val birthDateValid = verificationUtil.isBirthDateValid(_uiState.value.birthDate).status

        _uiState.value = _uiState.value.copy(
            usernameError = usernameValid.not(),
            userLastnameError = userLastnameValid.not(),
            emailError = emailValid.not(),
            birthDateError = birthDateValid.not()
        )

        viewModelScope.launch {
            if (usernameValid && userLastnameValid && emailValid && birthDateValid) {

                val survey = Survey(
                    first_name = _uiState.value.username,
                    last_name = _uiState.value.userLastname,
                    birth_date = _uiState.value.birthDate,
                    email = uiState.value.email
                )
                save(survey)
                validationEvent.emit(ValidationEvent.Success)
            }
        }
    }
}