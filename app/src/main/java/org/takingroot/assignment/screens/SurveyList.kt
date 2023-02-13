package org.takingroot.assignment.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
//import org.takingroot.assignment.domain.UIEvent
//import org.takingroot.assignment.domain.ValidationEvent
import org.takingroot.assignment.domain.UIEvent
import org.takingroot.assignment.domain.ValidationEvent
import org.takingroot.assignment.viewmodels.SurveyViewModel

//@Composable
//fun SurveyList() {
//    Text(text = "Show the surveys here")
//}
@Composable
fun SurveyList(
    surveyFormViewModel: SurveyViewModel = viewModel()
) {
    val state = surveyFormViewModel.uiState.value
    val context = LocalContext.current
    val localFocus = LocalFocusManager.current
    LaunchedEffect(key1 = context) {
        surveyFormViewModel.validationEvent.collect { event ->
            when(event) {
                is ValidationEvent.Success -> {
                    Toast
                        .makeText(context,"All inputs are valid", Toast.LENGTH_SHORT)
                        .show()
                }


            }
        }
    }
    Column(
//        modifier = Modifier
////            .fillMaxSize()
//            .padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
    ){
        FormInputField(
            label = "First Name",
            onTextChanged = {
//                bankFormViewModel.onEvent(UIEvent.AccountChanged(it))
                surveyFormViewModel.onEvent(UIEvent.NameChanged(it))

            },
//            keyboardType = KeyboardType.Text,
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
//                bankFormViewModel.onEvent(UIEvent.ConfirmAccountChanged(it))
                surveyFormViewModel.onEvent(UIEvent.LastNameChanged(it))

            },
//            keyboardType = KeyboardType.NumberPassword,
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
        FormInputField(
            label = "Owner name",
            onTextChanged = {
                surveyFormViewModel.onEvent(UIEvent.NameChanged(it))
            },
            isError = state.hasNameError,
            imeAction = ImeAction.Done,
            onNext = {},
            onDone = {
                localFocus.clearFocus()
            }
        )
//        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                surveyFormViewModel.onEvent(UIEvent.Submit)
            },
        ) {
            Text("Submit")
        }
    }
}
