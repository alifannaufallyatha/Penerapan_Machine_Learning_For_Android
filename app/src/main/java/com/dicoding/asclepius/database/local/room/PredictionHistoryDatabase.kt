package com.dicoding.asclepius.database.local.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.database.local.entity.PredictionHistoryEntity


@Database(entities = [PredictionHistoryEntity::class], version = 1, exportSchema = false)
abstract class PredictionHistoryDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao

    companion object {
        @Volatile
        private var instance: PredictionHistoryDatabase? = null
        fun getInstance(context: Context): PredictionHistoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PredictionHistoryDatabase::class.java, "History.db"
                ).build()
            }
    }
}