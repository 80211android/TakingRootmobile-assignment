package org.takingroot.assignment.viewmodels.map

import org.takingroot.assignment.models.Survey
import org.takingroot.assignment.networking.UserResponse

class SurveyMapper {

    fun mapToSurvey(response: UserResponse): Survey {
        return Survey(
            id = response.id,
            first_name = response.first_name,
            last_name = response.last_name,
            birth_date = response.birth_date,
            email = response.email
        )
    }
}