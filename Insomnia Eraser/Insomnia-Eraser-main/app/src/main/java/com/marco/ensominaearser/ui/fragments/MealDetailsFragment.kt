package com.marco.ensominaearser.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.grocery.groceryshop.utilites.serializable
import com.marco.ensominaearser.R
import com.marco.ensominaearser.data.pojo.Meal
import com.marco.ensominaearser.databinding.FragmentMealDetailsBinding
import com.marco.ensominaearser.ui.MainActivity
import com.marco.ensominaearser.viewmodel.MealsViewModel


class MealDetailsFragment : Fragment() {
    private lateinit var binding:FragmentMealDetailsBinding
    private val viewModel: MealsViewModel by viewModels<MealsViewModel>()
    private var mealVideo= ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMealDetailsBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.collapsingToolBar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolBar.setExpandedTitleColor(resources.getColor(R.color.white))

        binding.collapsingToolBar.title = requireArguments().getString("mealName")!!
        Glide.with(requireContext()).load(requireArguments().getString("mealPhoto")!!).into(binding.mealIv)
        getMealDetails(requireArguments().getString("id")!!)
        onYoutubeClick(mealVideo)

        return binding.root
    }

    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
    private fun onYoutubeClick(url:String){
        binding.youtubeTv.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent);
        }
    }


    private fun getMealDetails(id:String){
        viewModel.getMealDetails(id).observe(viewLifecycleOwner){
            if (it!=null){
                binding.progressBar.visibility = View.INVISIBLE
                binding.categoryTv.text ="Category : ${it.strCategory}"
                binding.locationTv.text="Location : ${it.strArea}"
                binding.contentTv.text = it.strInstructions
                mealVideo = it.strYoutube.toString()
            }

        }
    }


}