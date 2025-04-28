package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val path: String,
    val type: String,
    val size: Long,
    val lastModified: Date,
    val isFavorite: Boolean = false,
    val lastAccessed: Date = Date(),
    val isRecent: Boolean = false
) 