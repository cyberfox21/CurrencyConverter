package com.tatyanashkolnik.currencyconverter

import android.app.Activity
import android.os.Bundle

class ResultActivity : Activity() {

    private var name = resources.getStringArray(R.array.list_of_сurrencies)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)
    }


    private fun generateList(size : Int) : List<Card> {
        val list = ArrayList<Card>()

        for(i in 0 until size) {


            //val item = Card( parametres )


            //list += item

        }
        return list
    }
}
