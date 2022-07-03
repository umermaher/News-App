package com.example.newsapp2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp2.databinding.ArticleItemBinding
import com.example.newsapp2.databinding.SaveArticleItemBinding
import com.example.newsapp2.models.Article

class SavedArticlesAdapter (private val context: Context, private val listener: OnItemClickListener):
    RecyclerView.Adapter<SavedArticlesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SaveArticleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: SavedArticlesAdapter.ViewHolder, position: Int) {
        val model=articles[position]
        holder.binding.apply {
            Glide.with(context).load(model.urlToImage).into(articleImage)
            saveArticleTitle.text=model.title
            saveArticleAuthor.text=model.source?.name
            saveArticleTime.text=model.publishedAt
            saveArticleContent.text=model.content
            root.setOnClickListener {
                listener.onItemClick(model)
            }
        }
    }

    override fun getItemCount(): Int = articles.size

    interface OnItemClickListener{
        fun onItemClick(article: Article)
    }

    inner class ViewHolder(val binding: SaveArticleItemBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback=object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url==newItem.url
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem==newItem
    }
    private val differ= AsyncListDiffer(this,differCallback)
    var articles:List<Article>
        get() = differ.currentList
        set(value){differ.submitList(value)}
}