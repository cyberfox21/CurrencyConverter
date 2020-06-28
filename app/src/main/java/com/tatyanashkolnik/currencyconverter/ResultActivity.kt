package com.tatyanashkolnik.currencyconverter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.result.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ResultActivity : Activity() {

    private lateinit var arrayString : Array<String>
    private lateinit var listOfCards : List<Card>
    private lateinit var mapFromMainActivity : HashMap<String, Double>
    private lateinit var map : HashMap<String, Double>
    private lateinit var fromCurrency : String
    private var fromAmount : Double = 1.0
    private var amountToCalculate : Double = 1.0
    private var fromUSD : Boolean  = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)
        arrayString = resources.getStringArray(R.array.list_of_сurrencies)

        getIntent(intent)
        map = deleteNecessaryItem(fromCurrency, mapFromMainActivity)

        listOfCards = generateList()

        listOfResults.adapter = CardAdapter(listOfCards)
        listOfResults.layoutManager = LinearLayoutManager(this)
        listOfResults.setHasFixedSize(true)

    }
    private fun getIntent (intent : Intent){
        fromCurrency = intent.getStringExtra("from").toString()
        fromAmount = intent.getDoubleExtra("quantity", 1.0)
        mapFromMainActivity = intent.getSerializableExtra("map") as HashMap<String, Double>
        Log.i("Result", fromCurrency)
    }
    private fun deleteNecessaryItem (delete : String, dictionary : HashMap<String, Double>) : HashMap<String, Double>{
        amountToCalculate = dictionary[delete]!!
        dictionary.remove(delete)
        return dictionary
    }
    private fun calculateAmount (from : Double, to : Double, quantity : Double, fromUSD : Boolean, toUSD : Boolean) : Double {
        var result : Double
        if(from == to) {result = quantity}
        else if(fromUSD == true) {result = quantity * to}
        else if(toUSD == true) {result = quantity * (1 / from)}
        else {result = quantity * (1 / from) * to}
        return result
    }
    private fun generateList() : List<Card> {
        if(fromCurrency == "USD") {fromUSD = true}
        var toUSD = false
        val list = ArrayList<Card>()
        for (i in map){
            if (i.key == "USD" && fromUSD == false) {toUSD = true}
            else {toUSD = false}
            val item = Card(fromCurrency, fromAmount, i.key, String.format("%.2f", calculateAmount(amountToCalculate, i.value, fromAmount, fromUSD, toUSD)))
            list += item
        }
        return list
    }
}
