package com.example.newsapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapp2.R
import com.example.newsapp2.databinding.FragmentArticleBinding
import com.example.newsapp2.databinding.FragmentBreakingNewsBinding
import com.example.newsapp2.viewmodels.NewsViewModel

class ArticleFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    private var _binding: FragmentArticleBinding?=null
    private val binding get() = _binding!!
    private val args:ArticleFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding=FragmentArticleBinding.inflate(layoutInflater)
//        viewModel = (activity as MainActivity).viewModel!!
        val article=args.article
        binding.webView.apply {
            webViewClient= WebViewClient()
            loadUrl(article.url)
        }
        return binding.root
    }
}
