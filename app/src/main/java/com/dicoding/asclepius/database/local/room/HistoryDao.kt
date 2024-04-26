package com.dicoding.asclepius.database.local.room

import android.gesture.Prediction
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.asclepius.database.local.entity.PredictionHistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY createdAt DESC")
    fun getPredictionHistorys(): LiveData<List<PredictionHistoryEntity>>

//    @Query("SELECT * FROM history where urlImage")
//    fun getHistory(): LiveData<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistory(history: List<PredictionHistoryEntity>)

    @Update
    fun updateHistory(history: PredictionHistoryEntity)

    @Query("DELETE FROM history")
    fun deleteAll()
}