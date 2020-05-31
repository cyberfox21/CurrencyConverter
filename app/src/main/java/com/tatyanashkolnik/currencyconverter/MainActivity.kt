package com.tatyanashkolnik.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = "https://openexchangerates.org/api/latest.json?app_id=dd182aaa13c14ecd9bcccc4c4b2f4527"
        val request : String = AsyncTaskGetCurrentRatesJson().execute(url).get()
        Log.i("Result", request)
//        val gson = Gson()
//        val json = "{\"k1\":\"v1\",\"k2\":\"v2\"}"
//        var map: Map<String, String> = HashMap()
//        map = gson.fromJson<out Map>(json, map.javaClass)
//        Log.i("MyResult","map".toString())
    }

    inner class AsyncTaskGetCurrentRatesJson : AsyncTask <String, String, String>(){
        override fun doInBackground(vararg url: String?): String {
            var text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use {it.reader().use{reader -> reader.readText()}}
            }finally {
                connection.disconnect()
            }
            return text
        }

//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            //handleJson(result)
//        }

    }

//    private fun handleJson(jsonString: String?) {
//        val jsonArray = JSONArray(jsonString)
//        val list = ArrayList
//    }


}

