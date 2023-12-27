package com.example.pump.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.pump.R

// AEFilter activity
class AEFilter : AppCompatActivity() {

    // UI elements for selecting filters (replace with your actual UI elements)
    private lateinit var typeSpinner: Spinner
    private lateinit var muscleSpinner: Spinner
    private lateinit var difficultySpinner: Spinner
    private lateinit var searchBar: EditText
    private lateinit var applyFilterButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aefilter) // Replace with your layout file

        // Initialize UI elements (replace with your actual view IDs)
        typeSpinner = findViewById(R.id.typeSpinner)
        muscleSpinner = findViewById(R.id.muscleSpinner)
        difficultySpinner = findViewById(R.id.difficultySpinner)
        searchBar = findViewById(R.id.searchBar)
        applyFilterButton = findViewById(R.id.applyFilterButton)

        // Get the arrays from resources
        val exerciseTypes = resources.getStringArray(R.array.exercise_types)
        val muscleGroups = resources.getStringArray(R.array.muscle_groups)
        val difficultyLevels = resources.getStringArray(R.array.difficulty_levels)

        // Create adapters for spinners
        val exerciseTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseTypes)
        val muscleGroupAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, muscleGroups)
        val difficultyLevelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultyLevels)

        // Set dropdown layout style
        exerciseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        muscleGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        difficultyLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapters to spinners
        typeSpinner.adapter = exerciseTypeAdapter
        muscleSpinner.adapter = muscleGroupAdapter
        difficultySpinner.adapter = difficultyLevelAdapter

        val backButton: Button = findViewById(R.id.baButton)
        backButton.setOnClickListener {
            // Start CreateWorkoutActivity
            val intent = Intent(this, CreateWorkoutActivity::class.java)
            startActivity(intent)
        }
        // Set click listener for the apply filter button
        applyFilterButton.setOnClickListener {
            // Handle filter selection and send back the result
            onFiltersSelected()
        }
    }

    // Example method to handle filter selection and send back the result
    private fun onFiltersSelected() {
        // Obtain the selected filters
        val selectedType = typeSpinner.selectedItem.toString()
        val selectedMuscle = muscleSpinner.selectedItem.toString()
        val selectedDifficulty = difficultySpinner.selectedItem.toString()
        val searchInput = searchBar.text.toString()

        // Create an Intent to send back the selected filters
        val resultIntent = Intent()
        resultIntent.putExtra("EXERCISE_TYPE", selectedType)
        resultIntent.putExtra("MUSCLE_GROUP", selectedMuscle)
        resultIntent.putExtra("DIFFICULTY", selectedDifficulty)
        resultIntent.putExtra("SEARCH_INPUT", searchInput)

        // Set the result code and the result Intent
        setResult(RESULT_OK, resultIntent)

        // Finish the activity
        finish()
    }

}


