package com.dicoding.asclepius.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class PredictionHistoryEntity (
    @field:ColumnInfo(name = "urlImage")
    @field:PrimaryKey
    val urlImage: String,

    @field:ColumnInfo(name = "predictResult")
    val predictResult: String,

    @field:ColumnInfo(name = "confidenceScore")
    val confidenceScore: String,

    @field:ColumnInfo(name = "createdAt")
    val createdAt: String
)