package com.marco.ensominaearser.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.grocery.groceryshop.utilites.serializable
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentSongsBinding
import com.marco.ensominaearser.data.pojo.Category
import com.marco.ensominaearser.ui.adapters.SongsAdapter
import com.marco.ensominaearser.utilites.onSongClickListener

class SongsFragment : Fragment(),onSongClickListener {
    private lateinit var binding:FragmentSongsBinding
    private lateinit var category: Category
    private var songsAdapter =  SongsAdapter(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSongsBinding.inflate(layoutInflater)
        category = requireArguments().serializable<Category>("category")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCategoryDetails()
        setupSongsRecyclerView()

            songsAdapter.onSongClick = {

                Log.d("clicked",it)
                findNavController().navigate(R.id.action_songsFragment_to_playerFragment)
            }

        return binding.root
    }

    private fun setCategoryDetails(){
        Glide.with(requireContext()).load(category.coverurl).into(binding.coverImageView)
        binding.nameTextView.text = category.name

    }
    private fun setupSongsRecyclerView(){
        songsAdapter.diff.submitList(category.songs)
        binding.songsListRecyclerView.apply {
            adapter = songsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    override fun OnSongClickLitener() {
        Log.d("clicked","it")
        findNavController().navigate(R.id.action_songsFragment_to_playerFragment)
    }
}