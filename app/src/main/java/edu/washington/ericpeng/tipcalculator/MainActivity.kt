package edu.washington.ericpeng.tipcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.text.Editable
import android.text.Selection
import android.util.Log
import android.view.View
import android.widget.*


class MainActivity : AppCompatActivity() {

    private lateinit var et : EditText
    private lateinit var btn : Button
    private lateinit var spin : Spinner
    private lateinit var tip : String
    private var items = arrayOf("10%", "15%", "18%", "20%")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.button)
        btn.isEnabled = false

        et = findViewById(R.id.editText)
        et.addTextChangedListener(tw)

        spin = findViewById(R.id.spinner)
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                tip = items[position]
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spin.adapter = adapter

        btn.setOnClickListener {
            val amount = et.text.trim().removePrefix("$").toString().toDouble().times(tip.trim().removeSuffix("%").toDouble()/100)

            Toast.makeText(this, "Tip Amount: $" + "%.2f".format(amount), Toast.LENGTH_SHORT).show()
        }
    }

    private var tw: TextWatcher = object : TextWatcher {

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            btn.isEnabled = true

            if (!s.toString().matches("^\\$(\\d{1,3}(,\\d{3})*|(\\d+))(\\.\\d{2})?$".toRegex())) {
                val userInput = "" + s.toString().replace("[^\\d]".toRegex(), "")
                val cashAmountBuilder = StringBuilder(userInput)

                while (cashAmountBuilder.length > 3 && cashAmountBuilder[0] == '0') {
                    cashAmountBuilder.deleteCharAt(0)
                }
                while (cashAmountBuilder.length < 3) {
                    cashAmountBuilder.insert(0, '0')
                }
                cashAmountBuilder.insert(cashAmountBuilder.length - 2, '.')

                et.removeTextChangedListener(this)
                et.setText(cashAmountBuilder.toString())

                et.setTextKeepState("$" + cashAmountBuilder.toString())
                Selection.setSelection(et.text, cashAmountBuilder.toString().length + 1)

                et.addTextChangedListener(this)
            }
        }
    }
}
