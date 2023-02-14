package org.takingroot.assignment.screens

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.util.*


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun FormInputDateField(
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    onTextChanged: (String) -> Unit,
    onNext: (KeyboardActionScope) -> Unit,
    onDone: (KeyboardActionScope) -> Unit,
    readOnly: Boolean = false,
    context: Context
) {
    var text by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }

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


    OutlinedTextField(
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    // focused
                    DatePickerDialog(
                        context,
                        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
//                mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"

                            val month = (mMonth + 1).toString()
                            val validMonth = if (month.length < 2) {
                                "0$month"
                            } else {
                                month
                            }

                            val day = mDayOfMonth.toString()
                            val validDay = if (day.length < 2) {
                                "0$day"
                            } else {
                                day
                            }

                            val dates = "${mYear}-${validMonth}-$validDay"
                            text = dates
                            val x = mMonth
                            onTextChanged(dates)

                        }, mYear, mMonth, mDay
                    ).show()
                } else {
                    // not focused
                    val x = it

                }
            }
            .fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
            onTextChanged(it)
        },
        label = {
            Text(label)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = onDone,
            onNext = onNext
        ),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
        ),
        isError = isError,
        readOnly = readOnly,
    )
}