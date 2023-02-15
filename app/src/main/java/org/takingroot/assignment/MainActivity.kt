package org.takingroot.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.takingroot.assignment.components.BottomBar
import org.takingroot.assignment.components.NavBar
import org.takingroot.assignment.screens.ResponseList
import org.takingroot.assignment.screens.SurveyList
import org.takingroot.assignment.ui.theme.MobileAssignmentTheme
import org.takingroot.assignment.viewmodels.SurveyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: SurveyViewModel by viewModels { SurveyViewModelFactory.Factory }

        setContent {
            MobileAssignmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home(viewModel)
                }
            }
        }
    }
}

@Composable
fun Home(viewModel: SurveyViewModel) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
        NavBar("Assignment")
        Box(modifier = Modifier.padding(8.dp)) {
            if (selectedTab == 0) {
                SurveyList()
            } else {
                ResponseList(viewModel)
            }
        }
        BottomBar(onTabSelected = { selectedTab = it })
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LocalContext.current.applicationContext?.let {
        val viewModel = SurveyViewModelFactory.Factory.create(SurveyViewModel::class.java)

        MobileAssignmentTheme {
            Home(viewModel)
        }
    }
}