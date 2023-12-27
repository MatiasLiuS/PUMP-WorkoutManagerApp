package com.example.pump.business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable

data class SetData(
    val weight: String = "",
    val reps: String = ""
): Serializable

data class ExerciseData(
    val name: String = "",
    val type: String = "",
    val muscle: String = "",
    val equipment: String = "",
    val difficulty: String = "",
    val sets: List<SetData> = mutableListOf()
)

data class WorkoutData(
    val title: String = "",
    val imageUrl: String = "",
    val date: String = "",
    val exercises: List<ExerciseData> = mutableListOf()
)


class WorkoutViewModel : ViewModel() {
    private val _workoutData = MutableLiveData<WorkoutData>()
    val workoutData: LiveData<WorkoutData> get() = _workoutData

    // Add other data and methods as needed for your application

    fun setWorkoutData(title: String, imageUrl: String, date: String, exercises: List<ExerciseData>) {
        _workoutData.value = WorkoutData(title, imageUrl, date, exercises)
    }

}
