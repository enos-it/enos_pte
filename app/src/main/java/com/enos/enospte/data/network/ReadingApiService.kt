package com.enos.enospte.data.network

import com.enos.enospte.data.model.Reading
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReadingApiService {

    @POST("api/measures")
    suspend fun postReadings(
        @Body readings: List<Reading>
    ): Response<ResponseBody>

    @GET("api/measures/{userId}")
    suspend fun getReadings(
        @Path("userId") userId: String
    ): Response<List<Reading>>

    companion object {
        // Static Base URL & Port for the server
        // "http://10.0.2.2:8080/" maps to localhost:8080 on your host machine from Android Emulator
        // Change to your server's static IP/Port if needed (e.g., "http://192.168.1.100:8080/")
        const val BASE_URL = "http://192.168.7.73:5000/"

        val instance: ReadingApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ReadingApiService::class.java)
        }
    }
}
