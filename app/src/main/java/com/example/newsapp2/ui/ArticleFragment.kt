package com.example.newsapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp2.R
import com.example.newsapp2.viewmodels.NewsViewModel

class ArticleFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = (activity as MainActivity).viewModel
        return inflater.inflate(R.layout.fragment_article, container, false)
    }
}