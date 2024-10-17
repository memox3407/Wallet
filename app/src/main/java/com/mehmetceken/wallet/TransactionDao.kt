package com.mehmetceken.wallet

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` WHERE strftime('%Y-%m', date / 1000, 'unixepoch') = :month")
    suspend fun getTransactionsByMonth(month: String): List<Transaction>

    @Query("SELECT strftime('%Y-%m', date / 1000, 'unixepoch') AS month, SUM(amount) AS totalAmount FROM `Transaction` GROUP BY month")
    suspend fun getMonthlyTransactions(): List<MonthlyTransaction>
}

