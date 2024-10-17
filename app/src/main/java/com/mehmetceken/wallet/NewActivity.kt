package com.mehmetceken.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NewActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MonthlyTransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = AppDatabase.getDatabase(this)
        transactionDao = db.transactionDao()

        CoroutineScope(Dispatchers.IO).launch {
            val monthlyTransactions = transactionDao.getMonthlyTransactions()
            withContext(Dispatchers.Main) {
                adapter = MonthlyTransactionAdapter(monthlyTransactions, transactionDao)
                recyclerView.adapter = adapter
            }
        }
    }
}
