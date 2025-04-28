package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY lastModified DESC")
    fun getAllDocuments(): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE isFavorite = 1 ORDER BY lastModified DESC")
    fun getFavoriteDocuments(): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE isRecent = 1 ORDER BY lastAccessed DESC LIMIT 10")
    fun getRecentDocuments(): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE type LIKE :type || '%' ORDER BY lastModified DESC")
    fun getDocumentsByType(type: String): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE id = :documentId LIMIT 1")
    suspend fun getDocumentById(documentId: Long): Document?

    @Query("SELECT * FROM documents WHERE path = :path LIMIT 1")
    suspend fun getDocumentByPath(path: String): Document?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document)

    @Update
    suspend fun updateDocument(document: Document)

    @Delete
    suspend fun deleteDocument(document: Document)

    @Query("UPDATE documents SET isFavorite = :isFavorite WHERE id = :documentId")
    suspend fun updateFavoriteStatus(documentId: Long, isFavorite: Boolean)

    @Query("UPDATE documents SET isRecent = :isRecent WHERE id = :documentId")
    suspend fun updateRecentStatus(documentId: Long, isRecent: Boolean)

    @Query("UPDATE documents SET lastAccessed = :timestamp WHERE id = :documentId")
    suspend fun updateLastAccessed(documentId: Long, timestamp: Long)

    @Query("DELETE FROM documents")
    suspend fun deleteAllDocuments()

    @Query("SELECT COUNT(*) FROM documents")
    suspend fun getDocumentCount(): Int
} 