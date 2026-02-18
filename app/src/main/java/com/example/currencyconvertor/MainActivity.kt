package com.example.currencyconvertor

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.currencyconvertor.R.layout.drop_down_list_item
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    private val egyptianPound="Egyptian Pound"
    private val americanDollar="American Dollar"
    private val aed="AED"
    private val gdb="GDB"
    private val euro="Euro"
    val coins=mapOf(
        americanDollar to 1.0,
        egyptianPound to 46.94,
        aed to 3.67,
        gdb to 0.74,
        euro to .88
    )

    lateinit var fromDropdownMenu: AutoCompleteTextView
    lateinit var toDropdownMenu: AutoCompleteTextView
    lateinit var convertButton: Button
    lateinit var amountET: TextInputEditText
    lateinit var resultET: TextInputEditText
    lateinit var toolbar: Toolbar


    @SuppressLint(/* ...value = */)
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
        toolbar.inflateMenu(R.menu.options_menu)


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
      toolbar = findViewById(R.id.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return true
    }

    //    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Use 'when' as an expression that returns a value
        return when (item.itemId) {
            R.id.settingAction -> {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                true // Indicate that the event was handled
            }

            R.id.ShareAction -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
                true // Indicate that the event was handled
            }

            R.id.contactus -> {
                Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show()
                true // Indicate that the event was handled
            }
            // The 'else' block is crucial for handling all other cases
            else -> super.onOptionsItemSelected(item)
        }
    }
//        when (item.itemId) {
//            R.id.settingAction -> {
//                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
//            }
//
//            R.id.ShareAction -> {
//                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
//            }
//
//            R.id.contactus -> {
//                Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

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
                Snackbar.make(this, convertButton, "Invalid currency", Snackbar.LENGTH_SHORT).show()
                resultET.setText("")
            }
            val result = amount * toValue / fromValue
            resultET.setText(String.format("%.4f", result))
        } else {
            Snackbar.make(this, convertButton, "Invalid currency", Snackbar.LENGTH_SHORT).show()
            resultET.setText("")
        }
    }
}
