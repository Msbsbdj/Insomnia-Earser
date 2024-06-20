package com.marco.ensominaearser.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.marco.ensominaearser.R
import com.marco.ensominaearser.data.pojo.Meal
import com.marco.ensominaearser.databinding.FragmentHealthyFoodBinding
import com.marco.ensominaearser.ui.MainActivity
import com.marco.ensominaearser.ui.adapters.DinnerMealsAdapter
import com.marco.ensominaearser.ui.adapters.MealsAdapter
import com.marco.ensominaearser.utilites.PreferenceManager
import com.marco.ensominaearser.viewmodel.MealsViewModel
import java.util.Locale


class HealthyFoodFragment : Fragment() {
    private val viewModel: MealsViewModel by viewModels<MealsViewModel>()
    private lateinit var binding: FragmentHealthyFoodBinding
    private  var lunchMealsAdapter =MealsAdapter()
    private  var dinnerMealsAdapter= DinnerMealsAdapter()
     val x = 5

//    private var categoriesList = arrayListOf("Beef","Chicken","Lamb","Miscellaneous","Pasta",
//        "Pork","Seafood","Goat")
    private lateinit var sharedPreference: PreferenceManager
    private lateinit var meal:Meal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHealthyFoodBinding.inflate(layoutInflater)
        sharedPreference = PreferenceManager(requireContext())
        dinnerMealsAdapter
        openTimePicker()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getRandomMeal()
        getLunchMeals()
        getDinnerMeals()
        onLunchMealClick()
        onDinnerMealClick()
        onRandomMealClick()
        return binding.root
    }

    private fun getRandomMeal(){
        viewModel.getRandomMeal("Breakfast").observe(viewLifecycleOwner) {
            if (it.meals.isNotEmpty()) {
                meal = it.meals.random()
                stopShimmerLoading()
                Glide.with(requireContext()).load(meal.strMealThumb)
                    .into(binding.randomMealIv)
            }
        }
    }
    private fun getLunchMeals(){
        viewModel.getLunchMeals("beef").observe(viewLifecycleOwner) {
            lunchMealsAdapter.diff.submitList(it.meals)
            binding.lunchMealsRv.apply {
                adapter = lunchMealsAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    GridLayoutManager.HORIZONTAL, false
                )
            }
        }
    }
    private fun getDinnerMeals(){
        viewModel.getDinnerMeals("Chicken").observe(viewLifecycleOwner) {
            dinnerMealsAdapter.diff.submitList(it.meals)
            binding.dinnerMealsRv.apply {
                adapter = dinnerMealsAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    GridLayoutManager.HORIZONTAL, false
                )
            }
        }
    }

    private fun onLunchMealClick(){
        lunchMealsAdapter.onMealClick = {
            val bundle = Bundle()
            bundle.putString("id",it.idMeal)
            bundle.putString("mealPhoto",it.strMealThumb)
            bundle.putString("mealName",it.strMeal)
            bundle.putString("mealArea",it.strArea)
            findNavController().navigate(R.id.action_healthyFoodFragment_to_mealDetailsFragment,bundle)

        }
    }

    private fun onDinnerMealClick(){
        dinnerMealsAdapter.onMealClick = {
            val bundle = Bundle()
            bundle.putString("id",it.idMeal)
            bundle.putString("mealPhoto",it.strMealThumb)
            bundle.putString("mealName",it.strMeal)
            bundle.putString("mealArea",it.strArea)
            findNavController().navigate(R.id.action_healthyFoodFragment_to_mealDetailsFragment,bundle)

        }
    }

    private fun onRandomMealClick(){
        binding.randomMealIv.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",meal.idMeal)
            bundle.putString("mealPhoto",meal.strMealThumb)
            bundle.putString("mealName",meal.strMeal)
            bundle.putString("mealArea",meal.strArea)
            findNavController().navigate(R.id.action_healthyFoodFragment_to_mealDetailsFragment,bundle)
        }
    }

    private fun stopShimmerLoading() {
        binding.randomMealShimmerIv.root.visibility = View.INVISIBLE
        binding.randomMealCard.visibility = View.VISIBLE
        binding.popularMealsShimmer.visibility = View.INVISIBLE
        binding.lunchMealsRv.visibility = View.VISIBLE
        binding.dinnerMealsShimmer.visibility = View.INVISIBLE
        binding.dinnerMealsRv.visibility = View.VISIBLE

    }

    @SuppressLint("SetTextI18n")
//    private fun openTimePicker() {
//        val isSystem24Hour = is24HourFormat(requireContext())
//        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
//        val picker = MaterialTimePicker.Builder()
//            .setTimeFormat(clockFormat)
//            .setHour(12)
//            .setMinute(0)
//            .setTitleText("When do you wake up from sleep?")
//            .build()
//        picker.show(childFragmentManager, "Tag")
//        picker.addOnPositiveButtonClickListener {
//            val hour = picker.hour
//            val min = picker.minute
//            val amPm: String = if (hour >= 12) "PM" else "AM"
//            var formattedHour: Int = hour % 12
//            if (formattedHour == 0) {
//                formattedHour = 12
//            }
//                val formattedBreakTime = String.format(Locale.getDefault(), "%02d:%02d %s", formattedHour, min+30, amPm)
//                val formattedLunchTime = String.format(Locale.getDefault(), "%02d:%02d %s", formattedBreakTime+4, min, amPm)
//                val formattedDinnerTime = String.format(Locale.getDefault(), "%02d:%02d %s", formattedLunchTime+8, min, amPm)
//
//                sharedPreference.putInt("time", hour)
//                binding.breakfastTv.text = "BreakFast ($formattedBreakTime)"
//                binding.lunchTv.text = "Lunch ($formattedLunchTime)"
//                binding.dinnerTv.text = "Dinner ($formattedDinnerTime)"
//
//
//        }
//
//    }

    private fun openTimePicker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("When do you wake up from sleep?")
            .build()
        picker.show(childFragmentManager, "Tag")
        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val min = picker.minute

            // Calculate the break, lunch, and dinner times in hours
            val breakHour = (hour + (min + 30) / 60) % 24
            val lunchHour = (breakHour + 4) % 24
            val dinnerHour = (lunchHour + 8) % 24

            // Calculate minutes properly
            val breakMin = (min + 30) % 60

            // Format the times correctly
            val formattedBreakTime = String.format(Locale.getDefault(), "%02d:%02d %s", breakHour % 12, breakMin, if (breakHour >= 12) "PM" else "AM")
            val formattedLunchTime = String.format(Locale.getDefault(), "%02d:%02d %s", lunchHour % 12, min, if (lunchHour >= 12) "PM" else "AM")
            val formattedDinnerTime = String.format(Locale.getDefault(), "%02d:%02d %s", dinnerHour % 12, min, if (dinnerHour >= 12) "PM" else "AM")

            sharedPreference.putInt("time", hour)
            binding.breakfastTv.text = "BreakFast ($formattedBreakTime)"
            binding.lunchTv.text = "Lunch ($formattedLunchTime)"
            binding.dinnerTv.text = "Dinner ($formattedDinnerTime)"
        }
    }

}