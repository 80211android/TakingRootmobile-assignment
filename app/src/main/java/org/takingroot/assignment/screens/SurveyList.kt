package org.takingroot.assignment.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
//import org.takingroot.assignment.domain.UIEvent
//import org.takingroot.assignment.domain.ValidationEvent
import kotlinx.coroutines.flow.collect
import org.takingroot.assignment.domain.UIEvent
import org.takingroot.assignment.domain.ValidationEvent
import org.takingroot.assignment.viewmodels.SurveyViewModel

//@Composable
//fun SurveyList() {
//    Text(text = "Show the surveys here")
//}
@Composable
fun SurveyList(
    bankFormViewModel: SurveyViewModel = viewModel()
) {
    val state = bankFormViewModel.uiState.value
    val context = LocalContext.current
    val localFocus = LocalFocusManager.current
    LaunchedEffect(key1 = context) {
        bankFormViewModel.validationEvent.collect { event ->
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
        BankInputField(
            label = "Account number",
            onTextChanged = {
                bankFormViewModel.onEvent(UIEvent.AccountChanged(it))
            },
            keyboardType = KeyboardType.Number,
            isError = state.hasAccountError,
            onNext = {
                localFocus.moveFocus(FocusDirection.Down)
            },
            onDone = {

            }
        )
        BankInputField(
            label = "Confirm account number",
            onTextChanged = {
                bankFormViewModel.onEvent(UIEvent.ConfirmAccountChanged(it))
            },
            keyboardType = KeyboardType.NumberPassword,
            isError = state.hasConfirmAccountError,
            onNext = {
                localFocus.moveFocus(FocusDirection.Down)
            },
            onDone = {}
        )
        BankInputField(
            label = "Bank Code",
            onTextChanged = {
                bankFormViewModel.onEvent(UIEvent.CodeChanged(it))
            },
            isError = state.hasCodeError,
            onNext = {
                localFocus.moveFocus(FocusDirection.Down)
            },
            onDone = {}
        )
        BankInputField(
            label = "Owner name",
            onTextChanged = {
                bankFormViewModel.onEvent(UIEvent.NameChanged(it))
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
                bankFormViewModel.onEvent(UIEvent.Submit)
            },
        ) {
            Text("Submit")
        }
    }
}
