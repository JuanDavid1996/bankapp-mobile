package com.example.bankapp.presentation.home.adapters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bankapp.R
import com.example.bankapp.contants.ModelConstants.Companion.CURRENT_ES
import com.example.bankapp.contants.ModelConstants.Companion.SAVING
import com.example.bankapp.contants.ModelConstants.Companion.SAVING_ES
import com.example.bankapp.repository.bank.models.EnrolledAccount


class EnrolledAccountHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.title)
    val number: TextView = view.findViewById(R.id.number)
    val type: TextView = view.findViewById(R.id.type)
}

class EnrolledAccountAdapter(
    var enrolledAccount: List<EnrolledAccount> = emptyList(),
    var selectable: Boolean = false,
    var onClick: ((enrolledAccount: EnrolledAccount, isChecked: Boolean) -> Unit)? = null
) :
    RecyclerView.Adapter<EnrolledAccountHolder>() {
    var lastIndexSelected = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrolledAccountHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        val view: View = inflater.inflate(R.layout.enrolled_account_item, parent, false);
        return EnrolledAccountHolder(view)
    }

    override fun onBindViewHolder(holder: EnrolledAccountHolder, position: Int) {
        val enrolledAccount = enrolledAccount[position]
        holder.title.text =
            if (enrolledAccount.name.isNotBlank()) enrolledAccount.name else "Sin Descripción"
        holder.type.text = if (enrolledAccount.accountType == SAVING) SAVING_ES else CURRENT_ES
        holder.number.text = enrolledAccount.accountNumber
        holder.itemView.setOnClickListener { onItemClick(position, enrolledAccount) }

        if (lastIndexSelected == position) {
            holder.itemView.background =
                ColorDrawable(ContextCompat.getColor(holder.itemView.context, R.color.teal_200));
        } else {
            holder.itemView.background = null
        }
    }

    private fun onItemClick(position: Int, enrolledAccount: EnrolledAccount) {
        if (selectable) toggleItem(position, enrolledAccount)
        onClick?.let { it(enrolledAccount, lastIndexSelected != RecyclerView.NO_POSITION) }
    }

    private fun toggleItem(position: Int, enrolledAccount: EnrolledAccount) {
        val tempLastItemSelected = lastIndexSelected
        lastIndexSelected = if (lastIndexSelected == position) {
            RecyclerView.NO_POSITION
        } else {
            position
        }
        if (tempLastItemSelected != RecyclerView.NO_POSITION) notifyItemChanged(
            tempLastItemSelected
        )
        notifyItemChanged(lastIndexSelected)
    }

    override fun getItemCount(): Int = enrolledAccount.size
}