package com.dicoding.asclepius.viewmodel

import android.gesture.Prediction
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.database.PredictionHistoryRepository
import com.dicoding.asclepius.database.local.entity.PredictionHistoryEntity

class ViewModelHistory (private val historyRepository: PredictionHistoryRepository) : ViewModel() {

    fun getHistoryFromLocal() = historyRepository.getHistory()

    fun insertHistoryToLocal(history: PredictionHistoryEntity) =
        historyRepository.insertHistoryToLocal(history)

}
