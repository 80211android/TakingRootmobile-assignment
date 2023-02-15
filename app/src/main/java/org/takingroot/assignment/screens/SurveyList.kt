package org.takingroot.assignment.screens

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.takingroot.assignment.domain.UIEvent
import org.takingroot.assignment.viewmodels.SurveyViewModel

@Composable
fun SurveyList(
    surveyFormViewModel: SurveyViewModel = viewModel()
) {
    val state = surveyFormViewModel.uiState.value
    val context: Context = LocalContext.current
    val localFocus = LocalFocusManager.current
    LaunchedEffect(key1 = context) {
        surveyFormViewModel.surveyViewModelEventsAndStates.collect { event ->
            when(event) {
                is SurveyViewModel.SurveyViewModelEventsAndStates.SuccessEvent -> {
                    Toast.makeText(context,"Form Successfully Saved.", Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
                else -> {}
            }
        }
    }

    Column {
        FormInputField(
            label = "First Name",
            onTextChanged = {
                surveyFormViewModel.onEvent(UIEvent.NameChanged(it))
            },
            isError = state.usernameError,
            onNext = {
                localFocus.moveFocus(FocusDirection.Down)
            },
            onDone = {

            }
        )
        FormInputField(
            label = "Last Name",
            onTextChanged = {
                surveyFormViewModel.onEvent(UIEvent.LastNameChanged(it))
            },
            isError = state.userLastnameError,
            onNext = {
                localFocus.moveFocus(FocusDirection.Down)
            },
            onDone = {}
        )
        FormInputField(
            label = "Email",
            onTextChanged = {
                surveyFormViewModel.onEvent(UIEvent.EmailChanged(it))
            },
            keyboardType = KeyboardType.Email,
            isError = state.emailError,
            onNext = {
                localFocus.moveFocus(FocusDirection.Down)
            },
            onDone = {}
        )
        FormInputDateField(
            context = context,
            label = "Birthdate",
            onTextChanged = {
                surveyFormViewModel.onEvent(UIEvent.BirthDateChanged(it))
            },
            isError = state.birthDateError,
            imeAction = ImeAction.Done,
            onNext = {},
            onDone = {
                localFocus.clearFocus()
            },
            readOnly = false
        )
//        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                surveyFormViewModel.onEvent(UIEvent.Submit)
            },
        ) {
            Text("Submit")
        }

        Spacer(modifier = Modifier.size(100.dp))
    }
}
