package com.marco.ensominaearser.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentMeditationBinding
import com.marco.ensominaearser.data.pojo.Category
import com.marco.ensominaearser.ui.adapters.MusicCategoryAdapter
import com.marco.ensominaearser.utilites.MyExoplayer
import com.marco.ensominaearser.viewmodel.MusicViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class MeditationFragment : Fragment() {
    private lateinit var binding:FragmentMeditationBinding
    private val viewModel: MusicViewModel by viewModels<MusicViewModel>()
    private lateinit var categoryAdapter: MusicCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMeditationBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      CoroutineScope(Dispatchers.Main).launch{
          delay(2000L)
          viewModel.getCategories().collect{
              setupCategoryRv(it)
          }
      }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showPLayerView()
    }

    private fun showPLayerView(){
        binding.playerView.setOnClickListener {
            findNavController().navigate(R.id.action_meditationFragment_to_playerFragment )
        }
        MyExoplayer.getCurrentSong()?.let {
            binding.playerView.visibility = View.VISIBLE
            binding.songTitleTextView.text = "Now Playing: "+it.title
            Glide.with(requireContext()).load(it.coverUrl).into(binding.songCoverImageView)

        }?: kotlin.run {
            binding.playerView.visibility = View.GONE
        }
    }



    private fun setupCategoryRv(categoriesList:ArrayList<Category>){
        binding.categoriesShimmer.visibility = View.GONE
        categoryAdapter = MusicCategoryAdapter()
        categoryAdapter.diff.submitList(categoriesList)
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = categoryAdapter
        }
        categoryAdapter.onCategoryClick = {
            val categoryBundle = Bundle()
            categoryBundle.putSerializable("category",it)
            findNavController().navigate(R.id.action_meditationFragment_to_songsFragment,categoryBundle)

        }
    }


}