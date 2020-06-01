package com.tatyanashkolnik.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.os.AsyncTask
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import java.net.HttpURLConnection
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerTakeCurrency : Spinner
    private lateinit var spinnerGiveCurrency : Spinner

    private lateinit var map : MutableMap<String, Double>

    private var giveCurrency by Delegates.notNull<Double>()
    private var takeCurrency by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerTakeCurrency = findViewById(R.id.spinnerTakeCurrency)
        spinnerGiveCurrency = findViewById(R.id.spinnerGiveCurrency)

        val url = resources.getString(R.string.URL_AND_TANIUSHIN_API_KEY)
        map = getDictionary(AsyncTaskGetCurrentRatesJson().execute(url).get())


        spinnerTakeCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var resultText : String = returnTextToRequest(parent?.getItemAtPosition(position).toString())
                giveCurrency = getRequestToDictionary(resultText) // из какой валюты
                Log.i("Result", "give" + giveCurrency.toString())

                //ПРОПИСАТЬ ИЗМЕНЕНИЯ TEXTVIEW ВНИЗУ


            }
        }
        spinnerGiveCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var resultText: String = returnTextToRequest(parent?.getItemAtPosition(position).toString())
                takeCurrency = getRequestToDictionary(resultText) // в какую валюту
                Log.i("Result", "take" + takeCurrency.toString())


                //ПРОПИСАТЬ ИЗМЕНЕНИЯ TEXTVIEW ВНИЗУ


            }
        }

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
}

