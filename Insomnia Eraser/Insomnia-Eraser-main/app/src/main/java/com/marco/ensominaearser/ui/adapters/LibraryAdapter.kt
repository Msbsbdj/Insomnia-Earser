package com.marco.ensominaearser.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.marco.ensominaearser.databinding.BookCardBinding
import com.marco.ensominaearser.data.pojo.Book

class LibraryAdapter:RecyclerView.Adapter<LibraryAdapter.BookViewHolder>() {
    lateinit var onBookClick:((Book)->Unit)

    private val diffUtil = object: DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return (oldItem.name==newItem.name)
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
    val diff = AsyncListDiffer(this,diffUtil)
     inner class BookViewHolder(val binding: BookCardBinding):ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(BookCardBinding.inflate( LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(diff.currentList[position].bookPhoto)
            .into(holder.binding.bookIv)
        holder.binding.bookName.text = diff.currentList[position].name
        holder.binding.root.setOnClickListener {
            onBookClick.invoke(diff.currentList[position])
        }
    }
}