package com.example.pump.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.adapters.SetAdapter
import com.example.pump.business.WorkoutViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Serializable

class CSActivity : AppCompatActivity() {

    // UI components
    private lateinit var recyclerView: RecyclerView
    private lateinit var setAdapter: SetAdapter
    private lateinit var exerciseNameTextView: TextView
    private lateinit var addSetButton: FloatingActionButton
    private lateinit var addExerciseButton: Button
    private lateinit var backButton: Button
    private lateinit var viewModel: WorkoutViewModel


    // Exercise details
    private var exerciseName: String = ""
    private var exerciseType: String = ""
    private var exerciseMuscle: String = ""
    private var exerciseEquipment: String = ""
    private var exerciseDifficulty: String = ""
    private var requestCode: Int = -1

    // Log tag for debugging
    private val TAG = "CSActivity"

    // Request code for starting CreateWorkoutActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_csactivity)


        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)

        initUIComponents()

        // Retrieve values passed from ExerciseAdapter
        retrieveIntentData()

        // Display exercise name
        displayExerciseName()

        // Initialize RecyclerView and Adapter
        initRecyclerView()

        // Set Add Set button click listener
        setAddButtonClickListener()

        // Set click listener for the Add Set button
        addSetButton.setOnClickListener {
            addSetCard()
        }

        // Set click listener for the Add Set button
        addExerciseButton.setOnClickListener {
            sendExerciseData()
        }
    }

    // Function to initialize UI components
    private fun initUIComponents() {
        backButton = findViewById(R.id.backButton)
        addExerciseButton = findViewById(R.id.addExercise)
        addSetButton = findViewById(R.id.addSetButton)
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView)
    }

    // Function to retrieve intent data
    private fun retrieveIntentData() {
        requestCode = intent.getIntExtra("EXTRA_REQUEST_CODE", -1)
        exerciseName = intent.getStringExtra("EXTRA_EXERCISE_NAME") ?: ""
        exerciseType = intent.getStringExtra("EXTRA_EXERCISE_TYPE") ?: ""
        exerciseMuscle = intent.getStringExtra("EXTRA_EXERCISE_MUSCLE") ?: ""
        exerciseEquipment = intent.getStringExtra("EXTRA_EXERCISE_EQUIPMENT") ?: ""
        exerciseDifficulty = intent.getStringExtra("EXTRA_EXERCISE_DIFFICULTY") ?: ""

        // Log exercise details
        logExerciseDetails()
    }

    // Function to display exercise name
    private fun displayExerciseName() {
        exerciseNameTextView.text = exerciseName
    }

    // Function to initialize RecyclerView and Adapter
    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.setsRecyclerView)
        setAdapter = SetAdapter()
        recyclerView.adapter = setAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        updateAddButtonState()
    }

    // Function to set Add Set button click listener
    private fun setAddButtonClickListener() {
        setAdapter.setOnCardChangeListener {
            addSetButton.isEnabled = !setAdapter.getCardList().any { it.weight.isEmpty() || it.reps.isEmpty() }
        }
    }

    // Function to add a new set card
    private fun addSetCard() {
        if (setAdapter.itemCount < 5) {
            setAdapter.addCard()
            recyclerView.scrollToPosition(setAdapter.itemCount - 1)
        }
        updateAddButtonState()
    }

    // Function to update the Add Set button state
    private fun updateAddButtonState() {
        addSetButton.isEnabled = setAdapter.itemCount < 5
    }

    // Function to log exercise details
    private fun logExerciseDetails() {
        Log.d(TAG, "Exercise Name: $exerciseName")
        Log.d(TAG, "Exercise Type: $exerciseType")
        Log.d(TAG, "Exercise Muscle: $exerciseMuscle")
        Log.d(TAG, "Exercise Equipment: $exerciseEquipment")
        Log.d(TAG, "Exercise Difficulty: $exerciseDifficulty")
    }

    // Function to send exercise data to CreateWorkoutActivity
        private fun sendExerciseData() {
            val resultIntent = Intent()

            // Add exercise details to the intent
            resultIntent.putExtra("EXTRA_EXERCISE_NAME", exerciseName)
            resultIntent.putExtra("EXTRA_EXERCISE_TYPE", exerciseType)
            resultIntent.putExtra("EXTRA_EXERCISE_MUSCLE", exerciseMuscle)
            resultIntent.putExtra("EXTRA_EXERCISE_EQUIPMENT", exerciseEquipment)
            resultIntent.putExtra("EXTRA_EXERCISE_DIFFICULTY", exerciseDifficulty)

            // Add sets as a serializable object to the intent
            val sets = setAdapter.getCardList()
            resultIntent.putExtra("EXTRA_SETS", sets as Serializable)


            // Set the result and finish the activity
            setResult(RESULT_OK, resultIntent)
            finish()
    }
}
