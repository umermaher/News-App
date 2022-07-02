package com.example.newsapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp2.R
import com.example.newsapp2.adapter.ArticleAdapter
import com.example.newsapp2.databinding.FragmentBreakingNewsBinding
import com.example.newsapp2.models.Article
import com.example.newsapp2.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp2.utils.Resource
import com.example.newsapp2.utils.getNewsViewModel
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
        setUpRecyclerView()

//        viewModel = (activity as MainActivity).viewModel

        viewModel= getNewsViewModel(requireContext(),this)
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            //response -> Resource<NewsResponse>
            when (response) {
                is Resource.Success -> {
                    hidePb()
                    response.data?.let { newsResponse ->
                        newsAdapter.articles = newsResponse.articles.toList()
                        val totalPages=newsResponse.totalResults/ QUERY_PAGE_SIZE + 2
                        isLastPage= viewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                            binding.rvBreakingNews.setPadding(0,0 ,0,0)
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
        binding.breakingNewsFragmentPb.visibility = View.GONE
        binding.paginationProgressBar.visibility=View.GONE
        isLoading=false
    }
    private fun showPb() {
        binding.breakingNewsFragmentPb.visibility = View.VISIBLE
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    private fun setUpRecyclerView() = binding.rvBreakingNews.apply {
        newsAdapter= ArticleAdapter(requireContext(),createOnArticleClickListener())
        layoutManager=LinearLayoutManager(activity)
        setHasFixedSize(true)
        adapter=newsAdapter
        addOnScrollListener(this@BreakingNewsFragment.scrollListener)
    }

    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    private var scrollListener=object:RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate=isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling=false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }
    }

    private fun createOnArticleClickListener() = object : ArticleAdapter.OnItemClickListener{
        override fun onItemClick(article: Article) {
            val bundle=Bundle()
            bundle.putSerializable("article",article)
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
    }
}