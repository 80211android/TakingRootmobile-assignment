package org.takingroot.assignment.networking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class APIResponse<T>(
    @SerializedName("total")
    val total: Int,
    @SerializedName("limit")
    val limit: Int,

    @SerializedName("skip")
    val skip: Int,

    @SerializedName("data")
    val data: List<T>,
)


data class UserResponse(
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("first_name")
    val first_name: String,
    @SerializedName("last_name")
    @Expose
    val last_name: String,
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("birth_date")
    val birth_date: String
)
