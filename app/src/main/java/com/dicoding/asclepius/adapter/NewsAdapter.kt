package com.dicoding.asclepius.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.database.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemsNewsBinding
import com.dicoding.asclepius.helper.DateFormatter


class NewsAdapter(private val onItemClick: (ArticlesItem) -> Unit) : androidx.recyclerview.widget.ListAdapter<ArticlesItem, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)

        holder.itemView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
            holder.itemView.context.startActivity(intent)
        }
    }
    class MyViewHolder(val binding: ItemsNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: ArticlesItem){
            binding.tvTitleItem.text = news.title
            binding.tvItemPublishDate.text = DateFormatter.formatDate(news.publishedAt)
            Glide.with(binding.root).load(news.urlToImage).into(binding.ivPoster)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}