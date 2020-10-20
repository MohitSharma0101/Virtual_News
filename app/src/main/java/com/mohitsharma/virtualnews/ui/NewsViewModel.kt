package com.mohitsharma.virtualnews.ui

import androidx.lifecycle.ViewModel
import com.mohitsharma.virtualnews.repository.NewsRepository

class NewsViewModel(val newsRepository: NewsRepository):ViewModel() {
}