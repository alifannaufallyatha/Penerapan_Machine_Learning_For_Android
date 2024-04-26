package com.dicoding.asclepius.database

import android.gesture.Prediction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.asclepius.database.local.entity.PredictionHistoryEntity
import com.dicoding.asclepius.database.local.room.HistoryDao
import com.dicoding.asclepius.database.remote.retrofit.ApiService
import com.dicoding.asclepius.helper.AppExecutors


class PredictionHistoryRepository private constructor(
    private val apiService: ApiService,
    private val historyDao: HistoryDao,
    private val appExecutors: AppExecutors
){
    private val result = MediatorLiveData<Result<List<PredictionHistoryEntity>>>()

    fun getHistory(): LiveData<List<PredictionHistoryEntity>> {
        return historyDao.getPredictionHistorys()
    }

    fun insertHistoryToLocal(history: PredictionHistoryEntity){
        val historyList = ArrayList<PredictionHistoryEntity>()
        appExecutors.diskIO.execute{
            historyList.add(history)
//            historyDao.deleteAll()
            historyDao.insertHistory(historyList)
        }
    }

    companion object {
        @Volatile
        private var instance: PredictionHistoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            historyDao: HistoryDao,
            appExecutors: AppExecutors
        ): PredictionHistoryRepository =
            instance ?: synchronized(this) {
                instance ?: PredictionHistoryRepository(apiService, historyDao, appExecutors)
            }.also { instance = it }
    }
}
