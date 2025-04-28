package com.example.myapplication.repository

import com.example.myapplication.data.Document
import com.example.myapplication.data.DocumentDao
import kotlinx.coroutines.flow.Flow
import java.util.Date

class DocumentRepository(private val documentDao: DocumentDao) {
    val allDocuments: Flow<List<Document>> = documentDao.getAllDocuments()
    val favoriteDocuments: Flow<List<Document>> = documentDao.getFavoriteDocuments()
    val recentDocuments: Flow<List<Document>> = documentDao.getRecentDocuments()

    fun getDocumentsByType(type: String): Flow<List<Document>> {
        return documentDao.getDocumentsByType(type)
    }

    suspend fun getDocumentById(documentId: Long): Document? {
        return documentDao.getDocumentById(documentId)
    }

    suspend fun insertDocument(document: Document) {
        // Check if document with same path already exists
        val existingDocument = documentDao.getDocumentByPath(document.path)
        if (existingDocument == null) {
            documentDao.insertDocument(document)
        } else {
            // Update existing document with new information
            documentDao.updateDocument(document.copy(id = existingDocument.id))
        }
    }

    suspend fun updateDocument(document: Document) {
        documentDao.updateDocument(document)
    }

    suspend fun deleteDocument(document: Document) {
        documentDao.deleteDocument(document)
    }

    suspend fun updateFavoriteStatus(documentId: Long, isFavorite: Boolean) {
        documentDao.updateFavoriteStatus(documentId, isFavorite)
    }
    suspend fun updaterecentStatus(documentId: Long, isFavorite: Boolean) {
        documentDao.updateRecentStatus(documentId, isFavorite)
    }
    suspend fun updateLastAccessed(documentId: Long) {
        documentDao.updateLastAccessed(documentId, Date().time)
    }

    suspend fun deleteAllDocuments() {
        documentDao.deleteAllDocuments()
    }

    suspend fun getDocumentCount(): Int {
        return documentDao.getDocumentCount()
    }
} 