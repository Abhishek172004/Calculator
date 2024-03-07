package com.example.cal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.cal.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onclearClick(view: View) {

        binding.dataTv.text = ""
        lastNumeric = false
    }

    fun onbackClick(view: View) {
        val lastchar = binding.dataTv.text.toString().last()
        val listofoperator = listOf('+','-','*','/','%')
        if( lastchar in listofoperator){
            lastNumeric = true
        }

        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)

        try{
            var lastChar = binding.dataTv.text.toString().last()
            if (lastChar.isDigit()){
                onEqual()
            }
        }catch (e : Exception){
            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("last char error" , e.toString())
        }
    }

    fun onoperatorClick(view: View) {

        if( !stateError && lastNumeric){

            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()

        }
    }

    fun onDigitClick(view: View) {

        if(stateError){

            binding.dataTv.text = (view as Button).text
            stateError=false

        }else{

            binding.dataTv.append((view as Button).text )

        }
        lastNumeric = true
        onEqual()
    }

    fun onequalClick(view: View) {

        onEqual()
        binding.dataTv.text = binding.resultTv.text.toString().drop(1)
        binding.resultTv.visibility = View.GONE
        lastNumeric = true

    }

    fun onallclearClick(view: View) {

        binding.dataTv.text = ""
        binding.resultTv.text = ""
        binding.resultTv.visibility = View.GONE
        stateError = false
        lastDot = false
        lastNumeric = false

    }

    fun onEqual(){

        if( lastNumeric && !stateError){

            var txt = binding.dataTv.text.toString()
            expression = ExpressionBuilder(txt).build()


            try {
                var result = expression.evaluate()
                binding.resultTv.visibility = View.VISIBLE
                binding.resultTv.text = "=" + result.toString()

            }catch (ex: ArithmeticException){

                Log.e("evaluate error", ex.toString())
                binding.resultTv.text = "error"
                stateError = true
                lastNumeric = false

            }
        }
    }
}