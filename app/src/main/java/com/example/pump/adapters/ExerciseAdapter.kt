package com.example.pump.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.business.ExerciseData

class ExerciseAdapter(private val exerciseList: List<ExerciseData>, private val clickListener: OnItemClickListener) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    // Define the interface for the click listener
    interface OnItemClickListener {
        fun onItemClick(exercise: ExerciseData)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseNameTextView: TextView = itemView.findViewById(R.id.exercise_name_card_text)
        val typeTextView: TextView = itemView.findViewById(R.id.textView5)
        val equipmentTextView: TextView = itemView.findViewById(R.id.textView6)
        val setsTextView: TextView = itemView.findViewById(R.id.textView7)
        val muscleTextView: TextView = itemView.findViewById(R.id.textView8)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exerciseList[position]

        // Set exercise details to the corresponding TextViews
        holder.exerciseNameTextView.text = exercise.name
        holder.typeTextView.text = "Type: ${exercise.type}"
        holder.equipmentTextView.text = "Equipment: ${exercise.equipment}"
        holder.setsTextView.text = "Sets: ${exercise.sets.size.toString()}" // Assuming sets is a list
        holder.muscleTextView.text = "Muscle: ${exercise.muscle}"

        // Set click listener for the whole card
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(exercise)
        }
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}
