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

class SurveyViewModel(
    private val repository: ISurveyRepository,
    private val apiService: BaseAPIService
) : ViewModel() {
    val surveys = repository.surveys

    private var _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    val validationEvent = MutableSharedFlow<ValidationEvent>()

    init {
        this.refresh()
    }

    fun send(vararg surveys: Survey) = viewModelScope.launch(Dispatchers.IO) {
        withContext(this.coroutineContext) {
            surveys.forEach {
                apiService.response(it.name, it)
                repository.delete(it)
            }
            repository.fetchAll()
        }
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
                    accountNumber = event.account
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
                    ownerName = event.name
                )
            }
            is UIEvent.Submit -> {
                validateInputs()
            }
        }
    }

    private fun validateInputs() {
        val accountResult = Validator.validateAccountNumber(_uiState.value.accountNumber)
        val confirmAccountResult = Validator.validateConfirmAccountNumber(
            _uiState.value.accountNumber,
            _uiState.value.confirmAccountNumber
        )
        val codeResult = Validator.validateBankCode(_uiState.value.code)
        val nameResult = Validator.validateOwnerName(_uiState.value.ownerName)
        _uiState.value = _uiState.value.copy(
            hasAccountError = !accountResult.status,
            hasConfirmAccountError = !confirmAccountResult.status,
            hasCodeError = !codeResult.status,
            hasNameError = !nameResult.status,
        )
        val hasError = listOf(
            accountResult,
            confirmAccountResult,
            codeResult,
            nameResult
        ).any { !it.status }
        viewModelScope.launch {
            if (!hasError) {
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
                return SurveyViewModel(repository, RetrofitInstance.getInstance()) as T
            }
        }
    }
}