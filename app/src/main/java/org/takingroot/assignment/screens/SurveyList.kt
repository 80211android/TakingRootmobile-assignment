package org.takingroot.assignment.screens

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
//import org.takingroot.assignment.domain.UIEvent
//import org.takingroot.assignment.domain.ValidationEvent
import org.takingroot.assignment.domain.UIEvent
import org.takingroot.assignment.domain.ValidationEvent
import org.takingroot.assignment.viewmodels.SurveyViewModel
import java.util.*

//@Composable
//fun SurveyList() {
//    Text(text = "Show the surveys here")
//}
@Composable
fun SurveyList(
    surveyFormViewModel: SurveyViewModel = viewModel()
) {
    val state = surveyFormViewModel.uiState.value
    val context: Context = LocalContext.current
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

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
//                mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"

            val x = mMonth
        }, mYear, mMonth, mDay
    )



    Column {
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



//        Button(onClick = {
//            mDatePickerDialog.show()
//        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58)) ) {
//            Text(text = "Open Date Picker", color = Color.White)
//        }

        // Adding a space of 100dp height
        Spacer(modifier = Modifier.size(100.dp))
    }
}
