package com.example.newsapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp2.R
import com.example.newsapp2.adapter.ArticleAdapter
import com.example.newsapp2.databinding.FragmentBreakingNewsBinding
import com.example.newsapp2.databinding.FragmentSavedNewsBinding
import com.example.newsapp2.databinding.FragmentSearchNewsBinding
import com.example.newsapp2.models.Article
import com.example.newsapp2.utils.Constants
import com.example.newsapp2.utils.Resource
import com.example.newsapp2.utils.getNewsViewModel
import com.example.newsapp2.viewmodels.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private var _binding: FragmentSearchNewsBinding?=null
    private val binding get() = _binding!!
    private lateinit var newsAdapter:ArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentSearchNewsBinding.inflate(layoutInflater,container,false)
        setUpRecyclerView()
//        viewModel = (activity as MainActivity).viewModel
        viewModel= getNewsViewModel(requireContext(),this)

        var job:Job?=null
        binding.searchView.addTextChangedListener {editable->
            job?.cancel()
            job= MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            //response -> Resource<NewsResponse>
            when (response) {
                is Resource.Success -> {
                    hidePb()
                    response.data?.let { newsResponse ->
                        newsAdapter.articles = newsResponse.articles.toList()
                        val totalPages=newsResponse.totalResults/ Constants.QUERY_PAGE_SIZE + 2
                        isLastPage= viewModel.searchNewsPage == totalPages
                        if(isLastPage){
                            binding.rvSearchNews.setPadding(0,0 ,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hidePb()
                    response.message?.let { msg ->
                        Toast.makeText(requireContext(), "An error occurred: $msg", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showPb()
                }
            }
        }

        return binding.root
    }
    private fun hidePb() {
        binding.searchNewsFragmentPb.visibility = View.GONE
        binding.paginationProgressBar.visibility=View.GONE
        isLoading=false
    }
    private fun showPb() {
        binding.searchNewsFragmentPb.visibility = View.VISIBLE
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    private fun setUpRecyclerView() = binding.rvSearchNews.apply {
        newsAdapter= ArticleAdapter(requireContext(),createOnArticleClickListener())
        layoutManager= LinearLayoutManager(activity)
        setHasFixedSize(true)
        adapter=newsAdapter
        addOnScrollListener(this@SearchNewsFragment.scrollListener)
    }

    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    private val scrollListener=object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate=isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.searchNews(binding.searchView.text.toString())
                isScrolling=false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }
    }

    private fun createOnArticleClickListener() = object : ArticleAdapter.OnItemClickListener{
        override fun onItemClick(article: Article) {
            val bundle=Bundle()
            bundle.apply {
                putSerializable("article",article)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }
    }
}