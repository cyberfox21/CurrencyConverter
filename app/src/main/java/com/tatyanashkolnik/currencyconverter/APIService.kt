package com.tatyanashkolnik.currencyconverter

import com.tatyanashkolnik.currencyconverter.data.CurrencyResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

//const val TANIUSHIN_API_KEY = "dd182aaa13c14ecd9bcccc4c4b2f4527"
//https://openexchangerates.org/api/latest.json?app_id=dd182aaa13c14ecd9bcccc4c4b2f4527

interface APIService {
    @GET("api/latest.json?app_id=dd182aaa13c14ecd9bcccc4c4b2f4527")
    fun getResponce (@Query("app_id") id : String)
//    companion object {
//        fun create(): APIService {
//
//            val retrofit = Retrofit.Builder()
//                .addCallAdapterFactory(
//                    RxJava2CallAdapterFactory.create())
//                .addConverterFactory(
//                    GsonConverterFactory.create())
//                .baseUrl("https://openexchangerates.org/")
//                .build()
//
//            return retrofit.create(APIService::class.java)
//        }
//    }
}