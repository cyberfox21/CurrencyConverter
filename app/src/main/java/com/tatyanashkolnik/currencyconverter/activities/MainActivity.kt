@file:Suppress("DEPRECATION")

package com.tatyanashkolnik.currencyconverter.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.os.AsyncTask
import android.provider.Settings
import android.view.View
import android.widget.*
import java.net.HttpURLConnection
import java.net.URL
import android.view.ContextThemeWrapper
import com.tatyanashkolnik.currencyconverter.R
import com.tatyanashkolnik.currencyconverter.models.WordForDeclension
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Suppress("DEPRECATION")
class MainActivity : Activity() {

    private lateinit var editTextTakeQuantity: EditText
    private lateinit var textViewResultAmount: TextView
    private lateinit var textViewEnter: TextView
    private lateinit var textViewCourse: TextView
    private lateinit var textViewFrom: TextView
    private lateinit var textViewTo: TextView

    private lateinit var buttonResult: Button
    private lateinit var buttonMoreDetails: Button

    private lateinit var spinnerTakeCurrency: Spinner
    private lateinit var spinnerGiveCurrency: Spinner

    private lateinit var map: HashMap<String, Double>

    private var giveCurrency: Double = 0.0
    private var takeCurrency: Double = 0.0

    private var calculatedResult: Double = 0.0
    private var quantity: Double = 1.0

    private var isFromUSD: Boolean = false
    private var isToUSD: Boolean = false

    private var defaultText: String = "Рубль"

    private var resultText: String = ""

    private var textFrom: String = ""
    private var textTo: String = ""

    private var intentLTE: String = Settings.ACTION_DATA_ROAMING_SETTINGS

    private var intentWIFI: String = Settings.ACTION_WIFI_SETTINGS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isConnected()) {
            generateDialog()
        } else {
            setContentView(R.layout.main)

            spinnerTakeCurrency = findViewById(R.id.spinnerTakeCurrency)
            spinnerGiveCurrency = findViewById(R.id.spinnerGiveCurrency)
            editTextTakeQuantity =
                findViewById(R.id.editTextTakeQuantity)  // ввод количества переводимой валюты
            editTextTakeQuantity.maxLines = 1

            textViewCourse = findViewById(R.id.textViewCourse)  // курс

            textViewEnter = findViewById(R.id.textViewEnter)  // внизу сколько ввели
            textViewResultAmount =
                findViewById(R.id.textViewResultAmount)  // внизу сколько вывели TV результат перевода

            textViewFrom = findViewById(R.id.textViewFrom)  // внизу из какой валюты переводим
            textViewTo = findViewById(R.id.textViewTo)  // внизу в какую валюту переводим

            textViewFrom.text = defaultText
            textViewTo.text = defaultText
            buttonResult = findViewById(R.id.buttonResult)
            buttonMoreDetails = findViewById(R.id.buttonMoreDetails)

            val url = resources.getString(R.string.URL_AND_TANIUSHIN_API_KEY)
            map = getDictionary(AsyncTaskGetCurrentRatesJson().execute(url).get())

            buttonResult.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (editTextTakeQuantity.text.toString().isEmpty()) {
                        quantity = 1.0
                    } // Если пользователь ничего не ввёл
                    else {
                        quantity = editTextTakeQuantity.text.toString().toDouble()
                    }
                    textViewEnter.text = quantity.toString()
                    calculatedResult =
                        calculateAmount(
                            takeCurrency,
                            giveCurrency,
                            quantity,
                            isFromUSD,
                            isToUSD
                        )
                    textViewFrom.text = declension(quantity, textFrom)

                    Log.i("CHECKER", declension(quantity, textFrom))

                    textViewTo.text = declension(calculatedResult, textTo)

                    Log.i("CHECKER", declension(calculatedResult, textTo))

                    textViewResultAmount.text = String.format("%.2f", calculatedResult)
                    buttonMoreDetails.visibility = View.VISIBLE
                }
            })
            buttonMoreDetails.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val toResultActivity = Intent(this@MainActivity, ResultActivity::class.java)
                    //if (a > b) a else b
                    toResultActivity.putExtra(
                        "quantity",
                        if (editTextTakeQuantity.text.toString().isEmpty()) 1.0 else editTextTakeQuantity.text.toString().toDouble()
                    )
                    toResultActivity.putExtra("from", resultText)
                    toResultActivity.putExtra("map", map)
                    startActivity(toResultActivity)
                }
            })
            spinnerTakeCurrency.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        resultText =
                            returnTextToRequest(parent?.getItemAtPosition(position).toString())
                        if (resultText == "USD") {
                            isFromUSD = true
                        } else {
                            isFromUSD = false
                        }
                        takeCurrency = getRequestToDictionary(resultText) // из какой валюты
                        Log.i("ResultActivity", "take" + takeCurrency.toString())
                        textViewFrom.text = returnTextToUser(resultText)
                        textFrom = returnTextToUser(resultText)
                        textViewCourse.text = String.format(
                            "%.2f",
                            calculateCourse(takeCurrency, giveCurrency, isFromUSD, isToUSD)
                        )
                    }
                }
            spinnerGiveCurrency.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        var resultText: String =
                            returnTextToRequest(parent?.getItemAtPosition(position).toString())
                        if (resultText == "USD") {
                            isToUSD = true
                        } else {
                            isToUSD = false
                        }
                        giveCurrency = getRequestToDictionary(resultText) // в какую валюту
                        Log.i("ResultActivity", "give" + giveCurrency.toString())
                        textViewTo.text = returnTextToUser(resultText)
                        textTo = returnTextToUser(resultText)
                        textViewCourse.text = String.format(
                            "%.2f",
                            calculateCourse(takeCurrency, giveCurrency, isFromUSD, isToUSD)
                        )
                    }
                }
            spinnerTakeCurrency.adapter = ArrayAdapter<String>(
                this,
                R.layout.spinner_item,
                resources.getStringArray(R.array.list_of_сurrencies)
            )
            spinnerGiveCurrency.adapter = ArrayAdapter<String>(
                this,
                R.layout.spinner_item,
                resources.getStringArray(R.array.list_of_сurrencies)
            )
        }
    }
    private fun isConnected(): Boolean {
        val connectionManager: ConnectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

    private fun generateDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this,
            R.style.AlertDialogCustom
        ))
        val buttonClickLTE = { _: DialogInterface, _: Int ->
            val toLTESettings = Intent(intentLTE)
            finish()
            startActivity(toLTESettings)
        }
        val buttonClickWIFI = { _: DialogInterface, _: Int ->
            val toWIFISettings = Intent(intentWIFI)
            finish()
            startActivity(toWIFISettings)
        }
        with(builder)
        {
            setTitle(getString(R.string.dialog_title))
            setMessage(getString(R.string.dialog_message))
            setCancelable(false)
            setPositiveButton(
                getString(R.string.dialog_positive),
                DialogInterface.OnClickListener(function = buttonClickLTE)
            )
            setNegativeButton(getString(R.string.dialog_negative), DialogInterface.OnClickListener(function = buttonClickWIFI))
            show()
        }
    }

    inner class AsyncTaskGetCurrentRatesJson : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            var text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text =
                    connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                connection.disconnect()
            }
            return text
        }
    }

    private fun getDictionary(request: String): HashMap<String, Double> {
        val last = request.substringAfter("rates\": {")
        val beforeLast = last.substringBeforeLast("}")
        val afterLast = beforeLast.substringBeforeLast("}")

        val dontfinalresult = afterLast.replace(':', ',', true)
        val someresult = dontfinalresult.replace("\"", "")
        val lstKeysValues: List<String> = someresult.split(",").map { it -> it.trim() }

        val map = HashMap<String, Double>()
        for (i in 0 until lstKeysValues.size step 2) {
            map[lstKeysValues.get(i)] = (lstKeysValues.get(i + 1)).toDouble()
        }
        return map
    }

    private fun getRequestToDictionary(text: String): Double {
        var result: Double = map.getValue(text)
        return result
    }

    fun returnTextToRequest(string: String): String {
        var result: String = ""
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

    private fun returnTextToUser(string: String): String {
        var result: String = ""
        when (string) {
            "RUB" -> result = "Рубль"
            "USD" -> result = "Доллар"
            "EUR" -> result = "Евро"
            "GBP" -> result = "Фунт"
            "CNY" -> result = "Юань"
            "UAH" -> result = "Гривна"
        }
        return result
    }

    private fun calculateAmount(
        from: Double,
        to: Double,
        quantity: Double,
        fromUSD: Boolean,
        toUSD: Boolean
    ): Double {
        var result: Double
        if (from == to) {
            result = quantity
        } else if (fromUSD == true) {
            result = quantity * to
        } else if (toUSD == true) {
            result = quantity * (1 / from)
        } else {
            result = quantity * (1 / from) * to
        }
        return result
    }

    private fun calculateCourse(from: Double, to: Double, fromUSD: Boolean, toUSD: Boolean): Double {
        var result: Double
        if (from == to) {
            result = 1.00
        } else if (fromUSD == true) {
            result = to
        } else if (toUSD == true) {
            result = 1 * (1 / from)
        } else {
            result = 1 * (1 / from) * to
        }
        return result
    }
    private fun getArrayToDeclension() : ArrayList<WordForDeclension>{
        val infinitive = ArrayList<String>(Arrays.asList(*resources.getStringArray
            (R.array.list_of_сurrencies))) // именительный падеж
        val genitive = ArrayList<String>(Arrays.asList(*resources.getStringArray
            (R.array.list_of_сurrencies_1))) // родительный падеж
        val plural = ArrayList<String>(Arrays.asList(*resources.getStringArray
            (R.array.list_of_сurrencies_2))) // множественное число
        var arrayOfDeclensions = ArrayList<WordForDeclension>()
        for (i in 0 until infinitive.size) {
            val item =
                WordForDeclension(
                    infinitive.get(i),
                    genitive.get(i),
                    plural.get(i)
                )
            arrayOfDeclensions.add(item)
        }
        return arrayOfDeclensions
    }
    private fun declension(number : Double, word : String) : String{
        var array : ArrayList<WordForDeclension> = getArrayToDeclension()
        var num = number.toInt()
        var numberCases = arrayOf(2, 0, 1, 1, 1, 2)
        lateinit var result : String
        for (i in 0 until array.size) {
            if((array[i].infinitive).equals(word)){
                var rightStringIndex = if (num % 100 > 4 && num % 100 < 20) 2
                else numberCases[Math.min(num % 10, 5)]
                result = when(rightStringIndex){
                    0 -> array[i].infinitive
                    1 -> array[i].genitive
                    else -> array[i].plural
                }
                break
            }
        }
        Log.i("CHECKER", "declension " + result)
        return result
    }
}

