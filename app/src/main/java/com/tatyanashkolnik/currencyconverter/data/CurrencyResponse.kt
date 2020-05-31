package com.tatyanashkolnik.currencyconverter.data

data class CurrencyResponse(
    val base: String,
    val disclaimer: String,
    val license: String,
    val rates: Rates,
    val timestamp: Int
)