package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.database.remote.response.ArticlesItem
import com.dicoding.asclepius.database.remote.response.NewsResponse
import com.dicoding.asclepius.database.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelNews : ViewModel() {

    private val _news = MutableLiveData<NewsResponse>()
    val news: LiveData<NewsResponse> =_news

    private val _listNews = MutableLiveData<List<ArticlesItem>>()
    val listNews: LiveData<List<ArticlesItem>> =_listNews

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }

    fun getNews() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getNews("ed293f4c360c4051b2754ae7a4b44850")

        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _news.value = response.body()
                    _listNews.value = response.body()?.articles
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _isLoading.value = false
                _errorResponse.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}