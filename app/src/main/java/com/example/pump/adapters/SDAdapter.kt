package com.example.pump.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.business.SetData

class SDAdapter(private val setList: List<SetData>) : RecyclerView.Adapter<SDAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setNumberTextView: TextView = itemView.findViewById(R.id.setNumberTextView)
        val weightTextView: TextView = itemView.findViewById(R.id.weightTextView)
        val repsTextView: TextView = itemView.findViewById(R.id.repsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.set_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val set = setList[position]

        // Set set details to the corresponding TextViews
        holder.setNumberTextView.text = "Set ${position + 1}"
        holder.weightTextView.text = "Weight: ${set.weight}"
        holder.repsTextView.text = "Reps: ${set.reps}"
    }

    override fun getItemCount(): Int {
        return setList.size
    }
}
