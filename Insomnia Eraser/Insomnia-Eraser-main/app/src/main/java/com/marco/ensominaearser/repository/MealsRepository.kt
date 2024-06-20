package com.marco.ensominaearser.repository

import androidx.lifecycle.MutableLiveData
import com.marco.ensominaearser.data.pojo.Meal
import com.marco.ensominaearser.data.pojo.MealsList
import com.marco.ensominaearser.data.remote.MealsRetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealsRepository{
    private var randomMealsList:MutableLiveData<MealsList> = MutableLiveData()
    private var lunchMealsList:MutableLiveData<MealsList> = MutableLiveData()
    private var dinnerMealsList:MutableLiveData<MealsList> = MutableLiveData()
    private var meal:MutableLiveData<Meal> = MutableLiveData()

     suspend fun getBreakfastRandomMeal(category:String): MutableLiveData<MealsList> {
        MealsRetrofitInstance.api.getMealsByCategories(category).enqueue(object :Callback<MealsList>{
            override fun onResponse(p0: Call<MealsList>, p1: Response<MealsList>) {
                if (p1.isSuccessful){
                    randomMealsList.value = p1.body()
                }
            }
            override fun onFailure(p0: Call<MealsList>, p1: Throwable) {
                TODO("Not yet implemented")
            }
        })
        return randomMealsList
    }

    fun getLunchMeals(category:String): MutableLiveData<MealsList> {
        MealsRetrofitInstance.api.getMealsByCategories(category).enqueue(object :Callback<MealsList>{
            override fun onResponse(p0: Call<MealsList>, p1: Response<MealsList>) {
                if (p1.isSuccessful){
                    lunchMealsList.value = p1.body()
                }
            }
            override fun onFailure(p0: Call<MealsList>, p1: Throwable) {
                TODO("Not yet implemented")
            }
        })
        return lunchMealsList
    }

    fun getDinnerMeals(category: String):MutableLiveData<MealsList>{
        MealsRetrofitInstance.api.getMealsByCategories(category).enqueue(object :Callback<MealsList>{
            override fun onResponse(p0: Call<MealsList>, p1: Response<MealsList>) {
                if (p1.isSuccessful){
                    dinnerMealsList.value = p1.body()
                }
            }
            override fun onFailure(p0: Call<MealsList>, p1: Throwable) {
                TODO("Not yet implemented")
            }
        })
        return dinnerMealsList
    }


    fun getMealDetails(id:String):MutableLiveData<Meal>{
        MealsRetrofitInstance.api.getMealDetails(id).enqueue(object :Callback<MealsList>{
            override fun onResponse(p0: Call<MealsList>, p1: Response<MealsList>) {
                meal.value = p1.body()!!.meals[0]
            }

            override fun onFailure(p0: Call<MealsList>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })
        return meal
    }




}


