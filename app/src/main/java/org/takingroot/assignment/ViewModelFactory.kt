package org.takingroot.assignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.takingroot.assignment.models.AppDatabase
import org.takingroot.assignment.repositories.RemoteRepository
import org.takingroot.assignment.networking.Service
import org.takingroot.assignment.repositories.SurveyRepository
import org.takingroot.assignment.utils.VerificationUtil
import org.takingroot.assignment.viewmodels.SurveyViewModel
import org.takingroot.assignment.viewmodels.map.SurveyMapper

object ViewModelFactory {

    val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            // Get the Application object from extras
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

            val db = AppDatabase.getDatabase(application)
            val repository = SurveyRepository(db.surveyDao())
            val verificationUtil = VerificationUtil()
            val remoteRepository = RemoteRepository(Service.getInstance())
            return SurveyViewModel(repository, remoteRepository, verificationUtil, SurveyMapper()) as T
        }
    }
}