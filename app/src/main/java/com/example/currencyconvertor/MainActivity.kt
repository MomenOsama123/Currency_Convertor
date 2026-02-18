package com.example.currencyconvertor

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.currencyconvertor.R.layout.drop_down_list_item
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    // Fixed: Matched with strings.xml "Egyptian pound"
    private val egyptianPound = "Egyptian pound"
    private val americanDollar = "American Dollar"
    private val aed = "AED"
    private val gbp = "GBP" // Corrected from GDB
    private val euro = "Euro"

    private val coins = mapOf(
        americanDollar to 1.0,
        egyptianPound to 52.78,
        aed to 3.67,
        gbp to 0.74,
        euro to 0.88
    )

    private lateinit var fromDropdownMenu: AutoCompleteTextView
    private lateinit var toDropdownMenu: AutoCompleteTextView
    private lateinit var convertButton: Button
    private lateinit var amountET: TextInputEditText
    private lateinit var resultET: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        populateAutoComplete()

        // Reactive updates as you type
        amountET.addTextChangedListener {
            calculateResult()
        }

        // Reactive updates when selection changes
        fromDropdownMenu.setOnItemClickListener { _, _, _, _ ->
            calculateResult()
        }
        toDropdownMenu.setOnItemClickListener { _, _, _, _ ->
            calculateResult()
        }

        // Optional: Manual trigger if button is made visible
        convertButton.setOnClickListener {
            calculateResult()
        }
    }

    private fun populateAutoComplete() {
        val listOfCountries = listOf(egyptianPound, americanDollar, aed, gbp, euro)
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

        // Handle invalid number input
        if (amountText.isNotEmpty() && amount == null) {
            amountET.error = "Invalid number"
            resultET.setText("")
            return
        } else {
            amountET.error = null
        }

        if (amount == null) {
            resultET.setText("")
            return
        }

        val fromValue = coins[fromCurrency]
        val toValue = coins[toCurrency]

        if (fromValue != null && toValue != null) {
            if (fromValue == 0.0) {
                Snackbar.make(convertButton, "Invalid exchange rate", Snackbar.LENGTH_SHORT).show()
                resultET.setText("")
                return
            }
            val result = (amount * toValue) / fromValue
            resultET.setText(String.format("%.4f", result))
        } else {
            // Clear result if selection is incomplete
            resultET.setText("")
        }
    }
}