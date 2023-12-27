package com.example.pump.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.adapters.AEAdapter
import com.example.pump.adapters.OnExerciseSelectedListener
import com.example.pump.business.ExerciseItem
import com.example.pump.manager.AEManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AEActivity : AppCompatActivity(), OnExerciseSelectedListener {

    private lateinit var aeManager: AEManager
    private lateinit var exerciseAdapter: AEAdapter
    private lateinit var backButton: Button
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private var currentPage = 1
    private val itemsPerPage = 10
    private lateinit var pageInfoText: TextView
    private lateinit var errorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aeactivity)
        previousButton = findViewById(R.id.previousButton)
        backButton = findViewById(R.id.babckButton)
        nextButton = findViewById(R.id.nextButton)
        pageInfoText = findViewById(R.id.pageInfoText)
        errorText = findViewById(R.id.errorMessageText)

        // Retrieve values from intent
        val exerciseType = intent.getStringExtra("EXERCISE_TYPE")
        val muscleGroup = intent.getStringExtra("MUSCLE_GROUP")
        val difficulty = intent.getStringExtra("DIFFICULTY")
        val searchInput = intent.getStringExtra("SEARCH_INPUT")

        // Set up RecyclerView
        aeManager = AEManager(this)
        exerciseAdapter = AEAdapter(this, emptyList())
        val recyclerView: RecyclerView = findViewById(R.id.exercisesRecycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Set the activity as the listener
        exerciseAdapter.setOnExerciseSelectedListener(this)

        recyclerView.adapter = exerciseAdapter

        // Call the AEManager to get exercises based on the provided parameters
        loadExercises(exerciseType, muscleGroup, difficulty, searchInput)

        previousButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                loadExercises(exerciseType, muscleGroup, difficulty, searchInput)
            }
        }

        nextButton.setOnClickListener {
            if (exerciseAdapter.itemCount == itemsPerPage) {
                currentPage++
                loadExercises(exerciseType, muscleGroup, difficulty, searchInput)
            }
        }
        backButton.setOnClickListener {
            val intent = Intent(this@AEActivity, CreateWorkoutActivity::class.java)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun loadExercises(
        exerciseType: String?,
        muscleGroup: String?,
        difficulty: String?,
        searchInput: String?
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val offset = (currentPage - 1) * itemsPerPage
                val exercises = aeManager.getExercises(
                    exerciseType,
                    muscleGroup,
                    difficulty,
                    searchInput,
                    offset
                )

                if (exercises.isEmpty()) {
                    // Display error message
                    errorText.text = "No exercises found"
                    errorText.visibility = TextView.VISIBLE

                    // Hide the error message after 2 seconds
                    Handler(Looper.getMainLooper()).postDelayed({
                        errorText.visibility = TextView.GONE
                        // Navigate back to AEActivity
                        val intent = Intent(this@AEActivity, CreateWorkoutActivity::class.java)
                        setResult(RESULT_OK, intent)
                        finish()
                    }, 2000)
                } else {
                    // Update the adapter with the new data
                    exerciseAdapter.updateData(exercises)

                    // Update page info text
                    pageInfoText.text = "Page $currentPage"

                    // Enable or disable buttons based on conditions
                    previousButton.isEnabled = currentPage > 1
                    nextButton.isEnabled = exerciseAdapter.itemCount == itemsPerPage
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // Implement the interface method
    override fun onExerciseSelected(exercise: ExerciseItem) {
        // Handle the selected exercise here
        val intent = Intent(this, CreateWorkoutActivity::class.java)
        intent.putExtra("EXTRA_EXERCISE_NAME", exercise.name)
        intent.putExtra("EXTRA_EXERCISE_TYPE", exercise.type)
        intent.putExtra("EXTRA_EXERCISE_MUSCLE", exercise.muscle)
        intent.putExtra("EXTRA_EXERCISE_EQUIPMENT", exercise.equipment)
        intent.putExtra("EXTRA_EXERCISE_DIFFICULTY", exercise.difficulty)

        Log.d("HERE", "HERE ${exercise.name}")
        Log.d("HERE", "HERE ${exercise.type}")

  // Set the result to OK and pass the intent with data
        setResult(RESULT_OK, intent)
        // Finish the activity
        finish()

    }

}

