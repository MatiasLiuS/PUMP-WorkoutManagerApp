package com.example.pump.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.google.android.material.textfield.TextInputEditText
import java.io.Serializable

class SetAdapter : RecyclerView.Adapter<SetAdapter.SetViewHolder>() {

    private val MAX_SETS = 5
    private var cardList: MutableList<CardData> = mutableListOf()
    private var onCardChangeListener: (() -> Unit)? = null

    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.addsetcardlayout, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val cardData = cardList[position]

        // Find the TextInputEditText views in the layout
        val weightEditText = holder.itemView.findViewById<TextInputEditText>(R.id.weightEditText)
        val repsEditText = holder.itemView.findViewById<TextInputEditText>(R.id.repsEditText)
        val titleTextView = holder.itemView.findViewById<TextInputEditText>(R.id.titleTextView)

        // Set the values from the data object
        weightEditText.setText(cardData.weight)
        repsEditText.setText(cardData.reps)

        // Set the set number dynamically
        titleTextView.setText("Set #${position + 1}")

        // Add text change listener for each card
        weightEditText.addTextChangedListener { text ->
            cardData.weight = text.toString()
            onCardChangeListener?.invoke()
        }
        repsEditText.addTextChangedListener { text ->
            cardData.reps = text.toString()
            onCardChangeListener?.invoke()
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun addCard() {
        if (cardList.size < MAX_SETS) {
            cardList.add(CardData("", ""))
            notifyItemInserted(cardList.size - 1)
        }
    }

    fun setOnCardChangeListener(listener: () -> Unit) {
        onCardChangeListener = listener
    }

    // Helper method to get the list of cards
    fun getCardList(): List<CardData> {
        return cardList
    }
    data class CardData(var weight: String, var reps: String) : Serializable
}
