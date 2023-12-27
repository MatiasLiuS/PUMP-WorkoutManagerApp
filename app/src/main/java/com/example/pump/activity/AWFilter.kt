package com.example.pump.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.pump.R
import com.example.pump.business.Filters

class AWFilter : AppCompatActivity() {
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awfilter)

        backButton = findViewById(R.id.baButton)
        backButton.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        // CATEGORY SPINNER BAR
        val categorySpinner: Spinner = findViewById(R.id.CategorySpinner)
        val categoryNames = Filters.Categories.map { it.name }.toTypedArray()
        val categoryAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        var selectedCategoryId: Int? = null
        // Retrieve the selected value when an item is selected
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCategoryId = if (position == 0) {
                    // "N/A" is selected, handle accordingly
                    // In this case, you may choose not to pass any ID
                    null
                } else {
                    Filters.Categories[position].id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
                selectedCategoryId = null
            }
        }

        // MUSCLE SPINNER BAR
        val muscleSpinner: Spinner = findViewById(R.id.muscleSpinner)
        val muscleNames = Filters.Muscles.map { it.name_en }.toTypedArray()
        val muscleAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, muscleNames)
        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        muscleSpinner.adapter = muscleAdapter

        var selectedMuscleId: Int? = null
        // Retrieve the selected value when an item is selected
        muscleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMuscleId = if (position == 0) {
                    // "N/A" is selected, handle accordingly
                    // In this case, you may choose not to pass any ID
                    null
                } else {
                    Filters.Muscles[position].id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
                selectedMuscleId = null
            }
        }

        // EQUIPMENT SPINNER BAR
        val equipmentSpinner: Spinner = findViewById(R.id.equipmentSpinner)
        val equipmentNames = Filters.Equipments.map { it.name }.toTypedArray()
        val equipmentAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, equipmentNames)
        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        equipmentSpinner.adapter = equipmentAdapter

        var selectedEquipmentId: Int? = null
        // Retrieve the selected value when an item is selected
        equipmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedEquipmentId = if (position == 0) {
                    // "N/A" is selected, handle accordingly
                    // In this case, you may choose not to pass any ID
                    null
                } else {
                    Filters.Equipments[position].id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
                selectedEquipmentId = null
            }
        }

// APPLY FILTER BUTTON
        val applyFilterButton: Button = findViewById(R.id.applyFilterButton)
        applyFilterButton.setOnClickListener {
            // Get the selected values
            val selectedCategory = selectedCategoryId?.toString() ?: ""
            val selectedMuscle = selectedMuscleId?.toString() ?: ""
            val selectedEquipment = selectedEquipmentId?.toString() ?: ""

            // Create an intent to start AW2Activity
            val intent = Intent(this, AWActivity::class.java)


            // Add selected values as extras to the intent
            intent.putExtra("CATEGORY", selectedCategory)
            intent.putExtra("MUSCLE", selectedMuscle)
            intent.putExtra("EQUIPMENT", selectedEquipment)
            // In AEFilterActivity (B)


            // Start AW2Activity
            startActivity(intent)
        }
    }
}


