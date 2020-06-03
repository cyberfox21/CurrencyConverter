package com.tatyanashkolnik.currencyconverter

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.result_card.*

class ResultActivity : Activity() {

    private lateinit var arrayString : Array<String>
    private lateinit var map : HashMap<String, Double>
    private lateinit var fromCurrency : String
    private var fromAmount : Double = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)
        arrayString = resources.getStringArray(R.array.list_of_сurrencies)

        ///// ПОЛУЧИТЬ ИНТЕНТ

        fromCurrency = intent.getStringExtra("from").toString()
        fromAmount = intent.getDoubleExtra("quantity", 1.0)
        map = intent.getSerializableExtra("map") as HashMap<String, Double>

    }





    //// СКОПИРОВАТЬ ФУНКЦИЮ ПЕРЕВОДА




    private fun generateList(size : Int) : List<Card> {
        val list = ArrayList<Card>()

        for(i in 0 until size) {


            //val item = Card( parametres )


            //list += item

        }
        return list
    }
}
