package com.marco.ensominaearser.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.marco.ensominaearser.data.pojo.Meal
import com.marco.ensominaearser.databinding.MealCardBinding

class MealsAdapter:RecyclerView.Adapter<MealsAdapter.MealsViewHolder>() {
    lateinit var onMealClick:((Meal)->Unit)

    private val diffUtil = object: DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return (oldItem.idMeal==newItem.idMeal)
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
    val diff = AsyncListDiffer(this,diffUtil)

   inner class MealsViewHolder(val binding: MealCardBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
       return MealsViewHolder(MealCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return diff.currentList.size
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
       Glide.with(holder.itemView)
           .load(diff.currentList[position].strMealThumb)
           .into(holder.binding.imgPopularItem)
        holder.binding.root.setOnClickListener {
            onMealClick.invoke(diff.currentList[position])
        }
    }
}