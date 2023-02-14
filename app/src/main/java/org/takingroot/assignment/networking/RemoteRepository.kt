package org.takingroot.assignment.networking

import org.takingroot.assignment.models.Survey

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