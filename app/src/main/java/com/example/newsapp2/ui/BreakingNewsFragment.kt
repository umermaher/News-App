package com.example.newsapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp2.R
import com.example.newsapp2.adapter.ArticleAdapter
import com.example.newsapp2.databinding.FragmentBreakingNewsBinding
import com.example.newsapp2.models.Article
import com.example.newsapp2.viewmodels.NewsViewModel

class BreakingNewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private var _binding:FragmentBreakingNewsBinding?=null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: ArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentBreakingNewsBinding.inflate(layoutInflater,container,false)

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() = binding.rvBreakingNews.apply {
        newsAdapter= ArticleAdapter(requireContext(),createOnArticleClickListener())
        layoutManager=LinearLayoutManager(activity)
        setHasFixedSize(true)
        adapter=newsAdapter
    }

    private fun createOnArticleClickListener() = object : ArticleAdapter.OnItemClickListener{
        override fun onItemClick(article: Article) {

        }
    }
}