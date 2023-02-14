package org.takingroot.assignment.networking

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.takingroot.assignment.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Service {

    private val accountIdInterceptor = Interceptor {
        val request = it.request()
            .newBuilder()
            .addHeader("account_id", Constants.accountIdToken)
            .build()
        it.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(accountIdInterceptor)
        .build()

    fun getInstance(): ApiEndpoints {
        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        return Retrofit.Builder().apply {
            baseUrl(Constants.baseUrl)
            addConverterFactory(GsonConverterFactory.create(gson))
            client(client)
        }.build().create(ApiEndpoints::class.java)
    }
}