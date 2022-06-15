package com.koolbots.straightdrive.networks

import android.util.Log
import com.koolbots.straightdrive.models.ApiCallModel
import com.koolbots.straightdrive.models.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiService {
    fun callApi(apiData: ApiCallModel, onResult: () -> Unit){
        val DAO :ApiDAO= RetrofitInstance.instance.create(ApiDAO::class.java)
        DAO.callApi(apiData).enqueue(

            object : Callback<ApiResponse> {
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {


                    Log.d("response",t.message?:"Error")

                }
                override fun onResponse( call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    val status = response.body()?.body


                    Log.d("response",status?:"null")
                    onResult()
                }
            }
        )
    }
}