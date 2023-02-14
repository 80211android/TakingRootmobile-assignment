package org.takingroot.assignment.networking

import org.takingroot.assignment.models.Survey
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiEndpoints {
    @POST("survey/{type}")
    suspend fun response(@Path("type") type: String, @Body survey: Survey): UserResponse

    @GET("survey/user")
    suspend fun getUserSurveys(): Response<APIResponse<UserResponse>>
}