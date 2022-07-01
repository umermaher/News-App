package com.example.newsapp2.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp2.R
import com.example.newsapp2.adapter.ArticleAdapter
import com.example.newsapp2.databinding.FragmentSavedNewsBinding
import com.example.newsapp2.databinding.FragmentSearchNewsBinding
import com.example.newsapp2.models.Article
import com.example.newsapp2.utils.getNewsViewModel
import com.example.newsapp2.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private var _binding: FragmentSavedNewsBinding?=null
    private val binding get() = _binding!!
    private lateinit var newsAdapter:ArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentSavedNewsBinding.inflate(layoutInflater)
        setUpRecyclerView()
//        viewModel = (activity as MainActivity).viewModel
        viewModel= getNewsViewModel(requireContext(),this)
        viewModel.getSaveArticles().observe(viewLifecycleOwner){
            newsAdapter.articles=it
            binding.numArticlesTv.text="${it.size} Articles"
        }
        return binding.root
    }

    private fun setUpRecyclerView() = binding.rvSavedNews.apply {
        newsAdapter= ArticleAdapter(requireContext(),createOnArticleClickListener())
        layoutManager= LinearLayoutManager(activity)
        setHasFixedSize(true)
        adapter=newsAdapter

        ItemTouchHelper(getItemTouchHelperCallback()).attachToRecyclerView(this)
    }
    private fun createOnArticleClickListener() = object : ArticleAdapter.OnItemClickListener{
        override fun onItemClick(article: Article) {
            val bundle=Bundle()
            bundle.apply {
                putSerializable("article",article)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }
    }
    private fun getItemTouchHelperCallback() = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean  = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position=viewHolder.adapterPosition
            val article = newsAdapter.articles[position]
            viewModel.deleteArticle(article)
            Snackbar.make(binding.root,"Article deleted successfully!",Snackbar.LENGTH_LONG)
                .setAction("undo") {
                    viewModel.saveArticle(article)
                }.setActionTextColor(Color.WHITE).show()
        }
    }
}