package com.mehmetceken.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthlyTransactionAdapter(private val monthlyTransactions: List<MonthlyTransaction>, private val transactionDao: TransactionDao) :
    RecyclerView.Adapter<MonthlyTransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.monthly_transaction_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val monthlyTransaction = monthlyTransactions[position]
        holder.month.text = monthlyTransaction.month
        holder.totalAmount.text = monthlyTransaction.totalAmount.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val transactions = transactionDao.getTransactionsByMonth(monthlyTransaction.month)
            withContext(Dispatchers.Main) {
                holder.transactionList.adapter = TransactionAdapter(transactions)
                holder.transactionList.layoutManager = LinearLayoutManager(holder.itemView.context)
            }
        }
    }

    override fun getItemCount(): Int {
        return monthlyTransactions.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val month: TextView = itemView.findViewById(R.id.month)
        val totalAmount: TextView = itemView.findViewById(R.id.totalAmount)
        val transactionList: RecyclerView = itemView.findViewById(R.id.transactionList)
    }
}
