package com.marco.ensominaearser.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentPdfViewBinding
import java.io.File

class PdfViewFragment : Fragment() {
    private lateinit var binding: FragmentPdfViewBinding
    private lateinit var dirPath :String
    private lateinit var fileName:String
    private lateinit var   downloadedFile : File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPdfViewBinding.inflate(layoutInflater)
        dirPath = arguments?.getString("dirPath")!!
        fileName = arguments?.getString("fileName")!!
        downloadedFile = File(dirPath, fileName)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.bookView.fromFile(downloadedFile)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->

            }
            .load()

        return binding.root
    }


}