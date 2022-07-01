package com.example.newsapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.newsapp2.R
import com.example.newsapp2.adapter.ArticleAdapter
import com.example.newsapp2.models.Article
import com.example.newsapp2.viewmodels.NewsViewModel

class SavedNewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        viewModel = (activity as MainActivity).viewModel

        return inflater.inflate(R.layout.fragment_saved_news, container, false)
    }

    private fun createOnArticleClickListener() = object : ArticleAdapter.OnItemClickListener{
        override fun onItemClick(article: Article) {
            val bundle=Bundle()
            bundle.putSerializable("article",article)
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }
    }
}