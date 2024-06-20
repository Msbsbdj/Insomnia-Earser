package com.marco.ensominaearser.ui.fragments
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.grocery.groceryshop.utilites.serializable
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentBookDetailsBinding
import com.marco.ensominaearser.data.pojo.Book
import com.marco.ensominaearser.utilites.SupportFunctions
import java.io.File


class BookDetailsFragment : Fragment() {
    private lateinit var binding:FragmentBookDetailsBinding
    private lateinit var book: Book




    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = FragmentBookDetailsBinding.inflate(layoutInflater)

        book = requireArguments().serializable<Book>("book")!!

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
       savedInstanceState: Bundle?): View {
                 setBookDetails()
        binding.readBtn.setOnClickListener {
            SupportFunctions.showProgressBar(requireContext(),"Please Wait")
            readBook()
        }
        return binding.root
    }

    private fun setBookDetails(){
        binding.bookName.text = book.name
        binding.bookAuth.text = book.writerName
        binding.bookDescContent.text = book.bookDesc
        binding.ratingBar.rating = book.bookRate
        SupportFunctions.loadPicture(requireContext(),book.bookPhoto,binding.bookImage)
    }


    private fun readBook(){
        downloadPdfFromInternet(
            book.bookPdf,
            getRootDirPath(requireContext()),
            book.name
        )
    }


    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.initialize(requireContext())
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    SupportFunctions.hideDialog()
                    val bundle = Bundle()
                    bundle.putString("dirPath", dirPath)
                    bundle.putString("fileName", fileName)
                    findNavController().navigate(R.id.action_bookDetailsFragment_to_pdfViewFragment,bundle)
                }
                override fun onError(error: com.downloader.Error?) {

                }

            })
    }

    private fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }





}