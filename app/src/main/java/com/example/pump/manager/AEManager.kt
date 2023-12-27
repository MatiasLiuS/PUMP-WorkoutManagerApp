package com.example.pump.manager

import android.content.Context
import android.util.Log
import com.example.pump.R
import com.example.pump.business.ExerciseItem
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class AEManager(private val context: Context) {

    data class Exercise(
        val name: String,
        val type: String,
        val muscle: String,
        val equipment: String,
        val difficulty: String,
        val instructions: String
    )

    private val apiKey = context.getString(R.string.AE_api_key)

    suspend fun getExercises(
        exerciseType: String?,
        muscleGroup: String?,
        difficulty: String?,
        searchInput: String?,
        offset: Int
    ): List<ExerciseItem> {
        val url = buildApiUrl(exerciseType, muscleGroup, difficulty, searchInput, offset)

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("X-Api-Key", apiKey)
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .build()

        Log.d("AEManager", "Making API request to: $url")

        val response = withContext(Dispatchers.IO) {
            client.newCall(Request.Builder().url(url).build()).execute()
        }

        return if (response.isSuccessful) {
            response.body?.use { responseBody ->
                val responseString = responseBody.string()
                Log.d("AEManager", "API response body: $responseString")
                Gson().fromJson(responseString, Array<ExerciseItem>::class.java).toList()
            } ?: emptyList()
        } else {
            Log.e("AEManager", "API request failed with code: ${response.code}")
            Log.e("AEManager", "API response body: ${response.body?.string()}")
            emptyList()
        }
    }

    private fun buildApiUrl(
        exerciseType: String?,
        muscleGroup: String?,
        difficulty: String?,
        searchInput: String?,
        offset: Int
    ): String {
        return "https://api.api-ninjas.com/v1/exercises?type=$exerciseType&muscle=$muscleGroup&difficulty=$difficulty&name=$searchInput&offset=$offset"
    }
}
