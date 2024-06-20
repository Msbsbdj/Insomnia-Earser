package com.marco.ensominaearser.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marco.ensominaearser.data.pojo.Meal
import com.marco.ensominaearser.data.pojo.MealsList
import com.marco.ensominaearser.repository.MealsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealsViewModel:ViewModel() {
    private var mealsRepository = MealsRepository()

    private var _meal: MutableLiveData<MealsList> = MutableLiveData()
    private var _lunchMeals : MutableLiveData<MealsList> = MutableLiveData()
    private var _dinnerMeals : MutableLiveData<MealsList> = MutableLiveData()
    private var _mealDetails:MutableLiveData<Meal> = MutableLiveData()




    fun getRandomMeal(category:String):MutableLiveData<MealsList> {
        viewModelScope.launch {
          _meal =  mealsRepository.getBreakfastRandomMeal(category)
        }
        return _meal
    }


    fun getLunchMeals(category: String):MutableLiveData<MealsList>{
        viewModelScope.launch {
            _lunchMeals=  mealsRepository.getLunchMeals(category)
        }
        return _lunchMeals

    }


    fun getDinnerMeals(category: String):MutableLiveData<MealsList>{
        viewModelScope.launch {
            _dinnerMeals=  mealsRepository.getDinnerMeals(category)
        }
        return _dinnerMeals

    }

    fun getMealDetails(id:String):MutableLiveData<Meal>{
        viewModelScope.launch {
            _mealDetails=  mealsRepository.getMealDetails(id)
        }
        return _mealDetails

    }

}