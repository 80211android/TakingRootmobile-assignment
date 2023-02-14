package org.takingroot.assignment.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.takingroot.assignment.ViewModelFactory
import org.takingroot.assignment.models.Survey
import org.takingroot.assignment.viewmodels.SurveyViewModel

@Composable
fun ResponseList(viewModel: SurveyViewModel) {
    fun createSamples() {
        val surveys = (0..5).map {
            Survey(
                first_name = "custom",
                last_name = "custom",
                birth_date = "custom",
                email = "custom"
            )
        }
        viewModel.save(surveys = surveys.toTypedArray())
    }

    val surveys: List<Survey>? by viewModel.surveys.observeAsState()
    Column {
        Text("Responses", style = MaterialTheme.typography.titleMedium)

        Column {
            surveys?.forEach {
                Text(text = "Survey  of  ${it.first_name}")
            }
        }
        Row {
            Button(onClick = viewModel::refresh) {
                Text(text = "Refresh list")
            }
//            Button(onClick = { createSamples() }) {
//                Text(text = "Create samples")
//            }
        }
        Button(onClick = { viewModel.send(*surveys.orEmpty().toTypedArray()) }) {
            Text(text = "Sync")
        }
    }
}

@Preview
@Composable
fun ResponseListPreview() {
    LocalContext.current.applicationContext?.let {
        val viewModel = ViewModelFactory.Factory.create(SurveyViewModel::class.java)
        ResponseList(viewModel = viewModel)
    }
}