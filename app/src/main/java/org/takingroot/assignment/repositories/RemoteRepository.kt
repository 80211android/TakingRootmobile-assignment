package org.takingroot.assignment.repositories

import org.takingroot.assignment.models.Survey
import org.takingroot.assignment.networking.ApiEndpoints
import org.takingroot.assignment.networking.UserResponse

class RemoteRepository(
    private val service: ApiEndpoints
) {

    suspend fun sendSurvey(survey: Survey): UserResponse {
        return service.response(SURVEY_ENDPOINT_PATH, survey)
    }

    companion object {
        private const val SURVEY_ENDPOINT_PATH = "user"
    }
}