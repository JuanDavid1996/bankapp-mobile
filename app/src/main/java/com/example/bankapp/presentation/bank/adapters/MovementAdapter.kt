package com.example.bankapp.presentation.bank.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bankapp.R
import com.example.bankapp.contants.ModelConstants.Companion.MOVEMENT_DEPOSIT
import com.example.bankapp.contants.ModelConstants.Companion.TOP_UP_BY_TRANSFER
import com.example.bankapp.repository.bank.models.Movement
import com.example.bankapp.utils.toCurrency


class MovementHolder(view: View) : RecyclerView.ViewHolder(view) {
    val description: TextView = view.findViewById(R.id.description)
    val value: TextView = view.findViewById(R.id.value)
    val currency: TextView = view.findViewById(R.id.currency)
}

class MovementAdapter(var movements: List<Movement> = emptyList()) :
    RecyclerView.Adapter<MovementHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        val view: View = inflater.inflate(R.layout.movement_item, parent, false)
        return MovementHolder(view)
    }

    override fun onBindViewHolder(holder: MovementHolder, position: Int) {
        val movement = movements[position]

        holder.description.text = movement.description
        holder.currency.text = movement.currency
        holder.value.text = movement.amount.toCurrency()

        println("MOVEMENT ${movement.description} MOVEMENT ${movement.movement}")

        if (movement.movement == MOVEMENT_DEPOSIT || movement.movement == TOP_UP_BY_TRANSFER) {
            holder.value.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.positive)
            )
        } else {
            holder.value.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.negative)
            )
        }
    }

    override fun getItemCount(): Int = movements.size
}