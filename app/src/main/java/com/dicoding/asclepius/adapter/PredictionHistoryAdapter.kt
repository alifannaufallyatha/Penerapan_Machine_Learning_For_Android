package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.local.entity.PredictionHistoryEntity
import com.dicoding.asclepius.databinding.ItemsNewsBinding


class PredictionHistoryAdapter : ListAdapter<PredictionHistoryEntity, PredictionHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    class MyViewHolder(val binding: ItemsNewsBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(history: PredictionHistoryEntity) {
            binding.tvTitleItem.text = history.predictResult
            binding.tvItemPublishDate.text = history.createdAt
            Glide.with(itemView.context)
                .load(history.urlImage)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                .into(binding.ivPoster)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PredictionHistoryEntity> =
            object : DiffUtil.ItemCallback<PredictionHistoryEntity>() {
                override fun areItemsTheSame(oldItem: PredictionHistoryEntity, newItem: PredictionHistoryEntity): Boolean {
                    return oldItem.createdAt == newItem.createdAt
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: PredictionHistoryEntity, newItem: PredictionHistoryEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}