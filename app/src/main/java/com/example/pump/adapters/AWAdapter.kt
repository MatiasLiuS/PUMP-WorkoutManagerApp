package com.example.pump.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.manager.Exercise

// Adapter class for displaying exercises in a RecyclerView.
class AWAdapter(private var exercises: List<Exercise>) : RecyclerView.Adapter<AWViewHolder>() {

    // Inflates the layout for each item in the RecyclerView.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AWViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.awcardlayout, parent, false)
        return AWViewHolder(view)
    }

    // Binds the data to the ViewHolder at the specified position.
    override fun onBindViewHolder(holder: AWViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    // Updates the adapter data with new exercises.
    fun updateData(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    // Returns the total number of items in the data set held by the adapter.
    override fun getItemCount(): Int {
        return exercises.size
    }
}
