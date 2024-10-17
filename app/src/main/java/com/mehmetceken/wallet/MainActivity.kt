package com.mehmetceken.wallet

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var transactionDao: TransactionDao
    lateinit var amount: EditText
    lateinit var description: EditText
    lateinit var paymentMethod: Spinner
    lateinit var selectDate: Button
    lateinit var submit: Button
    var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDatabase.getDatabase(this)
        transactionDao = database.transactionDao()

        val button: Button = findViewById(R.id.openNewActivityButton)
        button.setOnClickListener {
            val intent = Intent(this, NewActivity::class.java)
            startActivity(intent)
        }

        amount = findViewById(R.id.amount)
        description = findViewById(R.id.description)
        paymentMethod = findViewById(R.id.paymentMethod)
        selectDate = findViewById(R.id.selectDate)
        submit = findViewById(R.id.submit)

        selectDate.setOnClickListener {
            val year = selectedDate.get(Calendar.YEAR)
            val month = selectedDate.get(Calendar.MONTH)
            val day = selectedDate.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, monthOfYear)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                selectDate.text = "$dayOfMonth/${monthOfYear + 1}/$year"
            }, year, month, day)
            datePicker.show()
        }

        submit.setOnClickListener {
            val enteredAmount = amount.text.toString().toDouble()
            val enteredDescription = description.text.toString()
            val selectedPaymentMethod = paymentMethod.selectedItem.toString()
            val transactionDate = selectedDate.time

            val transaction = Transaction(
                amount = enteredAmount,
                description = enteredDescription,
                date = transactionDate.time,
                paymentMethod = selectedPaymentMethod
            )

            lifecycleScope.launch {
                transactionDao.insertTransaction(transaction)
            }
        }
    }
}
