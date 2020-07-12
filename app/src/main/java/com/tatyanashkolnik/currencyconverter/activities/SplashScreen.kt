package com.tatyanashkolnik.currencyconverter.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.tatyanashkolnik.currencyconverter.R

class SplashScreen : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashsreen)
        Handler().postDelayed({
            val toMainActivity = Intent(this, MainActivity::class.java)
            startActivity(toMainActivity)
        }, 3000)
    }
}
