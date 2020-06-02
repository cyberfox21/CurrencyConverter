package com.tatyanashkolnik.currencyconverter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ResultActivity : Activity() {

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
