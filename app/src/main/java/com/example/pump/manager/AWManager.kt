package com.example.pump.manager

import android.content.Context
import android.util.Log
import com.example.pump.R
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

// Data classes for API responses
data class Exercise(val id: Int, val uuid: String, val name: String, val exercise_base: Int, val description: String, val created: String, val category: Int, val muscles: List<Int>, val muscles_secondary: List<Int>, val equipment: List<Int>, val language: Int, val categoryName: String, val muscleNames: List<String>, val equipmentNames: List<String>)
data class ExerciseApiResponse(val results: List<Exercise>?)
data class ExerciseCategory(val id: Int, val name: String?)
data class MuscleApiResponse(val results: List<Muscle>?)
data class Muscle(val id: Int, val name_en: String?)
data class EquipmentApiResponse(val results: List<Equipment>?)
data class Equipment(val id: Int, val name: String?)

// Main class for managing exercises
class AWManager(private val context: Context) {
    private val gson = Gson()
    private val okHttpClient: OkHttpClient = OkHttpClient()

    // Function to retrieve exercises from the API
    suspend fun retrieveExercises(
        selectedCategory: String,
        selectedMuscle: String,
        selectedEquipment: String,
        limit: Int = 10,
        offset: Int = 0
    ): List<Exercise> {
        val apiUrl =
            "https://wger.de/api/v2/exercise/?limit=$limit&offset=$offset&category=$selectedCategory&muscles=$selectedMuscle&equipment=$selectedEquipment&language=2"

        val apiKey = context.getString(R.string.AW_api_key)
        val request = Request.Builder()
            .url(apiUrl)
            .header("Authorization", "$apiKey")
            .build()

        return withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(request).execute()
            val responseBody: String? = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                // Parse the API response
                val apiResponse = gson.fromJson(responseBody, ExerciseApiResponse::class.java)
                // Map and modify exercises with additional information
                apiResponse.results?.map { exercise ->
                    val category = getCategoryName(exercise.category)
                    val muscles = getMuscleNames(exercise.muscles)
                    val equipment = getEquipmentNames(exercise.equipment)

                    exercise.copy(
                        categoryName = category,
                        muscleNames = muscles,
                        equipmentNames = equipment
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        }
    }

    // Function to retrieve category name based on category ID
    private suspend fun getCategoryName(categoryId: Int): String {
        val apiUrl = "https://wger.de/api/v2/exercisecategory/$categoryId/"
        val apiKey = "Token b2e44774552bae17948a3007546a1b5cf8ef435e"
        val request = Request.Builder()
            .url(apiUrl)
            .header("Authorization", "$apiKey")
            .build()

        return withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(request).execute()
            val responseBody: String? = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                // Parse the category response
                val category = gson.fromJson(responseBody, ExerciseCategory::class.java)
                category.name ?: "N/A"
            } else {
                "N/A"
            }
        }
    }

    // Function to retrieve muscle names based on muscle IDs
    private suspend fun getMuscleNames(muscleIds: List<Int>): List<String> {
        val apiUrl = "https://wger.de/api/v2/muscle/"
        val apiKey = "Token b2e44774552bae17948a3007546a1b5cf8ef435e"
        val muscleIdsString = muscleIds.joinToString(",")
        val request = Request.Builder()
            .url("$apiUrl?limit=&offset=&ids=$muscleIdsString&language=2")
            .header("Authorization", "$apiKey")
            .build()

        return withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(request).execute()
            val responseBody: String? = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                // Parse the muscle response
                val muscleResponse = gson.fromJson(responseBody, MuscleApiResponse::class.java)
                // Map and filter muscles based on muscle IDs
                muscleResponse.results?.mapNotNull { muscle ->
                    if (muscleIds.contains(muscle.id)) {
                        muscle.name_en ?: "N/A"
                    } else {
                        null
                    }
                } ?: emptyList()
            } else {
                emptyList()
            }
        }
    }

    // Function to retrieve equipment names based on equipment IDs
    private suspend fun getEquipmentNames(equipmentIds: List<Int>): List<String> {
        val apiUrl = "https://wger.de/api/v2/equipment/"
        val apiKey = "Token b2e44774552bae17948a3007546a1b5cf8ef435e"
        val request = Request.Builder()
            .url("$apiUrl?limit=&offset=&language=2")
            .header("Authorization", "$apiKey")
            .build()

        return withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(request).execute()
            val responseBody: String? = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                // Parse the equipment response
                val equipmentResponse = gson.fromJson(responseBody, EquipmentApiResponse::class.java)
                // Filter and map equipment based on equipment IDs
                equipmentResponse.results?.let { equipmentList ->
                    equipmentList.filter { it.id in equipmentIds }
                        .mapNotNull { equipment ->
                            equipment.name ?: "N/A"
                        }
                } ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}
