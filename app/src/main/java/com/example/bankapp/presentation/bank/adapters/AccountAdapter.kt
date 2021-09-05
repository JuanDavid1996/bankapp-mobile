package com.example.bankapp.presentation.bank.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bankapp.R
import com.example.bankapp.contants.ModelConstants.Companion.CURRENT_ES
import com.example.bankapp.contants.ModelConstants.Companion.SAVING
import com.example.bankapp.contants.ModelConstants.Companion.SAVING_ES
import com.example.bankapp.repository.bank.models.Account
import com.example.bankapp.utils.toCurrency


class AccountHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.title)
    val number: TextView = view.findViewById(R.id.number)
    val balance: TextView = view.findViewById(R.id.balance)
}

class AccountAdapter(
    var accounts: List<Account> = emptyList(),
    var onClick: ((account: Account) -> Unit)? = null
) :
    RecyclerView.Adapter<AccountHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        val view: View = inflater.inflate(R.layout.account_item, parent, false);
        return AccountHolder(view)
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        val account = accounts[position]
        holder.title.text = if (account.type == SAVING) SAVING_ES else CURRENT_ES
        holder.number.text = account.number
        holder.balance.text = account.balance.toCurrency()
        holder.itemView.setOnClickListener { onClick?.let { it(account) } }
    }

    override fun getItemCount(): Int = accounts.size
}