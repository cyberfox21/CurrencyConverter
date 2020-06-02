package com.tatyanashkolnik.currencyconverter

import android.app.Activity
import android.os.Bundle

class ResultActivity : Activity() {

    private lateinit var arrayString : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)
        arrayString = resources.getStringArray(R.array.list_of_сurrencies)


        ///// ПОЛУЧИТЬ ИНТЕНТ
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
