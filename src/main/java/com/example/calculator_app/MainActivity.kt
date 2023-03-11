package com.example.calculator_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculator_app.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.DataTv.text = binding.ResultTv.text.toString().drop(1)
    }

    fun onAllClearClick(view: View) {
        binding.DataTv.text = ""
        binding.ResultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.ResultTv.visibility = View.GONE
    }

    fun onDigitClick(view: View) {
        if (stateError) {
            binding.DataTv.text = (view as Button).text
            stateError =  false
        } else {
            binding.DataTv.append((view as Button).text)
        }

        lastNumeric = true
        onEqual()
    }

    fun onOperatorClick(view: View) {
        if (!stateError && lastNumeric){
            binding.DataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun onBackClick(view: View) {

        binding.DataTv.text = binding.DataTv.text.toString().dropLast(1)

        try {
            val lastChar = binding.DataTv.text.toString().last()

            if(lastChar.isDigit()){

                onEqual()
            }
        } catch (e: Exception){

            binding.ResultTv.text = ""
            binding.ResultTv.visibility = View.GONE
            Log.e("Last char error", e.toString())
        }
    }

    fun onClearClick(view: View) {
        binding.DataTv.text = ""
        lastNumeric = false
    }

    fun onEqual(){

        if (lastNumeric && !stateError) {

            val txt = binding.DataTv.text.toString()

            expression = ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()

                binding.ResultTv.visibility = View.VISIBLE

                binding.ResultTv.text = "=" + result.toString()
            } catch (ex: ArithmeticException) {

                Log.e("evaluate error", ex.toString())
                binding.ResultTv.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}