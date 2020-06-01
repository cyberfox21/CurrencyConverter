package com.tatyanashkolnik.currencyconverter

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.os.AsyncTask
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : Activity() {

    private lateinit var editTextTakeQuantity : EditText
    private lateinit var textViewResultAmount : TextView
    private lateinit var textViewEnter : TextView
    private lateinit var textViewCourse : TextView
    private lateinit var textViewFrom : TextView
    private lateinit var textViewTo : TextView

    private lateinit var buttonResult : Button

    private lateinit var spinnerTakeCurrency : Spinner
    private lateinit var spinnerGiveCurrency : Spinner

    private lateinit var map : MutableMap<String, Double>

    private var giveCurrency : Double = 0.0
    private var takeCurrency : Double = 0.0

    private var calculatedResult : Double = 0.0
    private var quantity : Double = 0.0

    private var isFromUSD : Boolean = false
    private var isToUSD : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        spinnerTakeCurrency = findViewById(R.id.spinnerTakeCurrency)
        spinnerGiveCurrency = findViewById(R.id.spinnerGiveCurrency)

        editTextTakeQuantity = findViewById(R.id.editTextTakeQuantity)  // ввод количества переводимой валюты

        textViewCourse = findViewById(R.id.textViewCourse)  // курс

        textViewEnter = findViewById(R.id.textViewEnter)  // внизу сколько ввели
        textViewResultAmount = findViewById(R.id.textViewResultAmount)  // внизу сколько вывели TV результат перевода

        textViewFrom = findViewById(R.id.textViewFrom)  // внизу из какой валюты переводим
        textViewTo = findViewById(R.id.textViewTo)  // внизу в какую валюту переводим

        buttonResult = findViewById(R.id.buttonResult)

        val url = resources.getString(R.string.URL_AND_TANIUSHIN_API_KEY)
        map = getDictionary(AsyncTaskGetCurrentRatesJson().execute(url).get())

        // когда нажимается кнопка

        quantity = editTextTakeQuantity.toString().toDouble()


        spinnerTakeCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var resultText : String = returnTextToRequest(parent?.getItemAtPosition(position).toString())
                if (resultText == "USD") {isFromUSD = true}
                else {isFromUSD = false}
                takeCurrency = getRequestToDictionary(resultText) // из какой валюты
                Log.i("Result", "take" + takeCurrency.toString())

                //ПРОПИСАТЬ ИЗМЕНЕНИЯ TEXTVIEW ВНИЗУ


            }
        }
        spinnerGiveCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var resultText: String = returnTextToRequest(parent?.getItemAtPosition(position).toString())
                if (resultText == "USD") {isToUSD = true}
                else {isToUSD = false}
                giveCurrency = getRequestToDictionary(resultText) // в какую валюту
                Log.i("Result", "give" + giveCurrency.toString())


                //ПРОПИСАТЬ ИЗМЕНЕНИЯ TEXTVIEW ВНИЗУ


            }
        }
        calculatedResult = calculateAmount(takeCurrency, giveCurrency, quantity, isFromUSD, isToUSD)
        textViewResultAmount.text = calculatedResult.toString()

    }

    inner class AsyncTaskGetCurrentRatesJson : AsyncTask <String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            var text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                connection.disconnect()
            }
            return text
        }
    }

    fun getDictionary (request : String) : MutableMap<String, Double>{
        val last = request.substringAfter("rates\": {")
        val beforeLast = last.substringBeforeLast("}")
        val afterLast = beforeLast.substringBeforeLast("}")

        val dontfinalresult = afterLast.replace(':', ',', true)
        val someresult = dontfinalresult.replace("\"", "")
        val lstKeysValues: List<String> = someresult.split(",").map { it -> it.trim() }

        val map = mutableMapOf<String, Double>()
        for(i in 0 until lstKeysValues.size step 2){
            map[lstKeysValues.get(i)] = (lstKeysValues.get(i+1)).toDouble()
        }
        return map
    }

    fun getRequestToDictionary (text : String) : Double {
        var result : Double = map.getValue(text)
        return result
    }

    fun returnTextToRequest (string : String) : String {
        var result : String = ""
        when (string) {
            "Рубль" -> result = "RUB"
            "Доллар" -> result = "USD"
            "Евро" -> result = "EUR"
            "Фунт" -> result = "GBP"
            "Юань" -> result = "CNY"
            "Гривна" -> result = "UAH"
        }
        return result
    }

    fun calculateAmount (from : Double, to : Double, quantity : Double, fromUSD : Boolean, toUSD : Boolean) : Double {
        var result : Double
        if(from == to) {result = quantity}
        else if(fromUSD == true) {result = quantity * to}
        else if(toUSD == true) {result = quantity * (1 / from)}
        else {result = quantity * (1 / from) * to}
        return result
    }
}

