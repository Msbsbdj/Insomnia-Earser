package com.marco.ensominaearser.data.remote

import com.marco.ensominaearser.data.pojo.MealsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Query

interface MealsApiService {
    @GET("random.php")
   suspend fun getRandomMeals(): Call<MealsList>
    @GET("Filter.php")
    fun getMealsByCategories(@Query("c") categoryName:String):Call<MealsList>
    @GET("lookup.php")
    fun getMealDetails(@Query("i") id:String):Call<MealsList>

}