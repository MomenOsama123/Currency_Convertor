package com.example.currencyconvertor

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.currencyconvertor.R.layout.drop_down_list_item
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private val egyptianPound="Egyptian Pound"
    private val americanDollar="American Dollar"
    private val aed="AED"
    private val gdb="GDB"
    private val euro="Euro"
    val coins=mapOf(
            americanDollar to 1.0,
            egyptianPound to 47.08,
            aed to 3.67,
            gdb to 0.74,
            euro to .88
    )

    lateinit var fromDropdownMenu: AutoCompleteTextView
    lateinit var toDropdownMenu: AutoCompleteTextView
    lateinit var convertButton: Button
    lateinit var amountET: TextInputEditText
    lateinit var resultET: TextInputEditText


    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()

        populateAutoComplete()

        amountET.addTextChangedListener(){
            calculateResult()
        }
        fromDropdownMenu.setOnItemClickListener { adapterView, view, i, l ->
            calculateResult()
        }
        toDropdownMenu.setOnItemClickListener { adapterView, view, i, l ->
            calculateResult()
        }
    }

    private fun populateAutoComplete() {
        val listOfCountries = listOf(egyptianPound, americanDollar, aed, gdb,euro)
        val adapter = ArrayAdapter(this, drop_down_list_item, listOfCountries)
        toDropdownMenu.setAdapter(adapter)
        fromDropdownMenu.setAdapter(adapter)
    }

    private fun initializeViews() {
        convertButton = findViewById(R.id.ConvertButton)
        amountET = findViewById(R.id.Amount)
        resultET = findViewById(R.id.ResultOutput)
        toDropdownMenu = findViewById(R.id.ConvertTo)
        fromDropdownMenu = findViewById(R.id.from_currency_menu)
    }
    @SuppressLint("DefaultLocale")
    private fun calculateResult() {
        val amountText = amountET.text.toString()
        val fromCurrency = fromDropdownMenu.text.toString()
        val toCurrency = toDropdownMenu.text.toString()

        val amount = amountText.toDoubleOrNull()
        if (amountText.isNotEmpty() && amount == null) {
            amountET.error = "Invalid number"
            resultET.setText("")
            return
        } else {
            amountET.error = null // Clear error
        }

        if (amount == null) {
            resultET.setText("")
            return
        }

        val fromValue = coins[fromCurrency]
        val toValue = coins[toCurrency]

        if (fromValue != null && toValue != null) {
            if (fromValue == 0.0) {
                resultET.setText("Error") // Should not happen with current data
                return
            }
            val result = amount * toValue / fromValue
            resultET.setText(String.format("%.4f", result))
        } else {
            resultET.setText("")
        }
    }
}
