package com.tatyanashkolnik.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = resources.getString(R.string.URL_AND_TANIUSHIN_API_KEY)
        var map : MutableMap<String, Double> = getDictionary(AsyncTaskGetCurrentRatesJson().execute(url).get())

        map.forEach { it ->
            Log.i("Result", "$it")
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
}

