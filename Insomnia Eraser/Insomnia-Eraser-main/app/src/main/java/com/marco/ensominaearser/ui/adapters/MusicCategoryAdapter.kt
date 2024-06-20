package com.marco.ensominaearser.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.marco.ensominaearser.databinding.CategoryItemRecyclerRowBinding
import com.marco.ensominaearser.data.pojo.Category

class MusicCategoryAdapter:RecyclerView.Adapter<MusicCategoryAdapter.CategoryViewHolder>() {
    lateinit var onCategoryClick:((Category)->Unit)

    private val diffUtil = object: DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return (oldItem.name==newItem.name)
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
    val diff = AsyncListDiffer(this,diffUtil)

    inner class CategoryViewHolder(val binding: CategoryItemRecyclerRowBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
       return CategoryViewHolder(CategoryItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
         return diff.currentList.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.nameTextView.text = diff.currentList[position].name
        Glide.with(holder.itemView)
            .load(diff.currentList[position].coverurl).apply {
                RequestOptions().transform(RoundedCorners(32))
            }
            .into(holder.binding.coverImageView)
        holder.binding.root.setOnClickListener {
            onCategoryClick.invoke(diff.currentList[position])
        }
    }
}