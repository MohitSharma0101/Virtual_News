package com.mohitsharma.virtualnews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohitsharma.virtualnews.repository.NewsRepository
import com.mohitsharma.virtualnews.ui.home.HomeViewModel

class NewsViewModelProviderFactory(val newsRepository: NewsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = NewsViewModel(newsRepository) as T
}