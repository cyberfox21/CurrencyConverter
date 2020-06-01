package com.tatyanashkolnik.currencyconverter

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