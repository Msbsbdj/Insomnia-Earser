package com.marco.ensominaearser.data.remote


import com.marco.ensominaearser.data.pojo.AiModelResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
interface ApiService {
    @POST("predict")
     fun getResult(@Body information:RequestBody):Call<AiModelResponse>
}