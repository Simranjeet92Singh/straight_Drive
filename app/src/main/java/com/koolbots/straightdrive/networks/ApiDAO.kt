package com.koolbots.straightdrive.networks

import com.koolbots.straightdrive.models.ApiCallModel
import com.koolbots.straightdrive.models.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiDAO {

    @Headers(
        "Content-Type: application/json",
        "x-api-key: 5Xc7GOkmmf4WiQa70jT042MxEUSpLzCP2zBK35CH")
    @POST("dev")
   public fun callApi(@Body apiCall: ApiCallModel): Call<ApiResponse>

}