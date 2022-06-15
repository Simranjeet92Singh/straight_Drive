package com.koolbots.straightdrive.networks

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitInstance {
    companion object{
         val client = OkHttpClient.Builder().build()
        val instance = Retrofit.Builder()
                .baseUrl("https://3yozk7sdx5.execute-api.us-west-1.amazonaws.com/") // change this IP for testing by your actual machine IP
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build()


    }
}