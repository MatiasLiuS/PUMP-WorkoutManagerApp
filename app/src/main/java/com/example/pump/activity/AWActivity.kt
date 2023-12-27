package com.example.pump.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.adapters.AWAdapter
import com.example.pump.manager.AWManager
import com.example.pump.manager.Exercise
import kotlinx.coroutines.launch

class AWActivity : AppCompatActivity() {
    private val limit = 10
    private var currentPage = 1

    private lateinit var previousButton: Button
    private lateinit var nextButton: Button
    private lateinit var exercisesRecycler: RecyclerView
    private lateinit var adapter: AWAdapter
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorMessageText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_workouts)

        // Initialize UI elements
        initializeViews()
        updatePageInfo()

        // Initialize AWManager for data handling
        val awManager = AWManager(this)

        // Initialize RecyclerView and its adapter
        adapter = AWAdapter(emptyList())
        exercisesRecycler.adapter = adapter

        // Retrieve data from the intent
        val selectedCategory = intent.getStringExtra("CATEGORY") ?: ""
        val selectedMuscle = intent.getStringExtra("MUSCLE") ?: ""
        val selectedEquipment = intent.getStringExtra("EQUIPMENT") ?: ""

        // Load initial data

        loadData(selectedCategory, selectedMuscle, selectedEquipment, currentPage)

        // Set up back button to navigate to the filter section
        val backButton: Button = findViewById(R.id.baButton)
        backButton.setOnClickListener {
            startActivity(Intent(this, AWFilter::class.java))
        }

        // Set up listeners for Previous and Next buttons
        previousButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                updatePageAndLoadData()
            }
        }

        nextButton.setOnClickListener {
            currentPage++
            updatePageAndLoadData()
        }
    }

    // Initialize UI elements
    private fun initializeViews() {
        errorMessageText = findViewById(R.id.errorMessageText)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        exercisesRecycler = findViewById(R.id.exercisesRecycler)
        exercisesRecycler.layoutManager = LinearLayoutManager(this)
        previousButton = findViewById(R.id.previousButton)
        nextButton = findViewById(R.id.nextButton)
    }

    // Update page information and reload data
    private fun updatePageAndLoadData() {
        updatePageInfo()
        loadData(
            intent.getStringExtra("CATEGORY") ?: "",
            intent.getStringExtra("MUSCLE") ?: "",
            intent.getStringExtra("EQUIPMENT") ?: "",
            currentPage
        )
        updateButtonState()
    }

    // Load workout data from AWManager
    private fun loadData(category: String, muscle: String, equipment: String, page: Int) {
        lifecycleScope.launch {
            try {
                // Show loading indicator
                loadingProgressBar.visibility = View.VISIBLE
                exercisesRecycler.visibility = View.GONE

                // Calculate offset
                val newOffset = (page - 1) * limit

                // Retrieve exercises from the API using intent values
                val exercises: List<Exercise> = AWManager(this@AWActivity).retrieveExercises(category, muscle, equipment, limit, newOffset)

                // Update adapter with new data
                adapter.updateData(exercises)

                // Disable "Next" button if less than 10 cards are loaded
                nextButton.isEnabled = exercises.size >= limit

                if (exercises.isEmpty()) {
                    // No data found, show error message and navigate back to filter section
                    errorMessageText.visibility = View.VISIBLE
                    Handler().postDelayed({
                        startActivity(Intent(this@AWActivity, AWFilter::class.java))
                    }, 2000)
                } else {
                    // Data found, hide error message
                    errorMessageText.visibility = View.GONE
                }

            } catch (e: Exception) {
                Log.e("AWActivity", "Error retrieving exercises: ${e.message}")
                // Handle or log the error as needed
            } finally {
                // Hide loading indicator
                loadingProgressBar.visibility = View.GONE
                exercisesRecycler.visibility = View.VISIBLE
            }
        }
    }

    // Update displayed page information
    private fun updatePageInfo() {
        val pageInfoText: TextView = findViewById(R.id.pageInfoText)
        pageInfoText.text = "Page $currentPage"
    }

    // Update state of Previous button based on current page
    private fun updateButtonState() {
        previousButton.isEnabled = currentPage > 1
    }
}
