package com.marco.ensominaearser.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentLibraryBinding
import com.marco.ensominaearser.ui.adapters.LibraryAdapter
import com.marco.ensominaearser.viewmodel.LibraryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {
    private lateinit var binding:FragmentLibraryBinding

    private val viewModel: LibraryViewModel by viewModels<LibraryViewModel>()
    private  var booksAdapter:LibraryAdapter =  LibraryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLibraryBinding.inflate(layoutInflater)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        CoroutineScope(Dispatchers.Main).launch {
            getBooksFromFireStore()
        }
        onBookCardClickListener()
        return binding.root
    }
    private suspend fun getBooksFromFireStore(){
        binding.bookRv.apply {
            adapter = booksAdapter
             layoutManager = GridLayoutManager(requireContext(),3, GridLayoutManager.VERTICAL,false)
        }
        viewModel.getLibraryBooks().collect{
            if (it.isNotEmpty()){
               dataState()
                booksAdapter.diff.submitList(it)
            }

        }
    }
    private fun onBookCardClickListener(){
        booksAdapter.onBookClick =  {
            val bundle = Bundle()
            bundle.putSerializable("book",it)
            findNavController().navigate(R.id.action_libraryFragment_to_bookDetailsFragment,bundle)
        }
    }

    private fun dataState(){
        binding.booksShimmer.visibility = View.GONE
        binding.bookRv.visibility = View.VISIBLE
    }

}