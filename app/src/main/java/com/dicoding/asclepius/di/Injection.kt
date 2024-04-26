package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.database.PredictionHistoryRepository
import com.dicoding.asclepius.database.local.room.PredictionHistoryDatabase
import com.dicoding.asclepius.database.remote.retrofit.ApiConfig
import com.dicoding.asclepius.helper.AppExecutors

object Injection {
    fun provideRepository(context: Context): PredictionHistoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = PredictionHistoryDatabase.getInstance(context)
        val dao = database.historyDao()
        val appExecutors = AppExecutors()
        return PredictionHistoryRepository.getInstance(apiService, dao, appExecutors)
    }
}