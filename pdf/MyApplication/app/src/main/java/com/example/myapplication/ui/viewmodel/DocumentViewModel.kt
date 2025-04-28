package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.Document
import com.example.myapplication.data.DocumentLoader
import com.example.myapplication.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File

class DocumentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DocumentRepository
    private val documentLoader: DocumentLoader
    val allDocuments: Flow<List<Document>>
    val favoriteDocuments: Flow<List<Document>>
    val recentDocuments: Flow<List<Document>>
    private val TAG = "DocumentViewModel"

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        val documentDao = AppDatabase.getDatabase(application).documentDao()
        repository = DocumentRepository(documentDao)
        documentLoader = DocumentLoader(application)

        // Add error handling to flows
        allDocuments = repository.allDocuments.catch { e ->
            Log.e(TAG, "Error loading all documents: ${e.message}", e)
            emit(emptyList())
        }

        favoriteDocuments = repository.favoriteDocuments.catch { e ->
            Log.e(TAG, "Error loading favorite documents: ${e.message}", e)
            emit(emptyList())
        }

        recentDocuments = repository.recentDocuments.catch { e ->
            Log.e(TAG, "Error loading recent documents: ${e.message}", e)
            emit(emptyList())
        }
    }

    fun getDocumentsByType(type: String): Flow<List<Document>> {
        return repository.getDocumentsByType(type).catch { e ->
            Log.e(TAG, "Error loading documents of type $type: ${e.message}", e)
            emit(emptyList())
        }
    }

    fun openDocument(documentId: Long) {
        viewModelScope.launch {
            try {
                val document = repository.getDocumentById(documentId)
                if (document == null) {
                    Log.e(TAG, "Document not found with ID: $documentId")
                    return@launch
                }

                val file = File(document.path)
                if (!file.exists()) {
                    Log.e(TAG, "File does not exist: ${document.path}")
                    return@launch
                }

                val uri = FileProvider.getUriForFile(
                    getApplication(),
                    "${getApplication<Application>().packageName}.provider",
                    file
                )

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, document.type)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                getApplication<Application>().startActivity(intent)
                
                // Update last accessed time
                repository.updateLastAccessed(documentId)
            } catch (e: Exception) {
                Log.e(TAG, "Error opening document: ${e.message}", e)
            }
        }
    }

    fun toggleFavorite(documentId: Long) {
        viewModelScope.launch {
            try {
                val document = repository.getDocumentById(documentId)
                if (document != null) {
                    repository.updateFavoriteStatus(documentId, !document.isFavorite)
                    Log.d(TAG, "Toggled favorite status for document ID: $documentId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error toggling favorite status: ${e.message}", e)
            }
        }
    }

    fun updateLastAccessed(documentId: Long) {
        viewModelScope.launch {
            try {
                val document = repository.getDocumentById(documentId)
                if (document != null) {
                    repository.updaterecentStatus(documentId, !document.isRecent)
                    Log.d(TAG, "Toggled favorite status for document ID: $documentId")
                }
                Log.d(TAG, "Updated last accessed time for document ID: $documentId")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating last accessed time: ${e.message}", e)
            }
        }
    }

    fun insertDocument(document: Document) = viewModelScope.launch {
        try {
            repository.insertDocument(document)
            Log.d(TAG, "Inserted document: ${document.name}")
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting document: ${e.message}", e)
        }
    }

    fun updateDocument(document: Document) = viewModelScope.launch {
        try {
            repository.updateDocument(document)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating document: ${e.message}", e)
        }
    }

    fun deleteDocument(document: Document) = viewModelScope.launch {
        try {
            repository.deleteDocument(document)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting document: ${e.message}", e)
        }
    }

    fun refreshDocuments() = viewModelScope.launch {
        try {
            _isLoading.value = true
            Log.d(TAG, "Starting document refresh")

            // Load documents directly
            val documents = documentLoader.loadDocuments()
            Log.d(TAG, "Loaded ${documents.size} documents")

            // Clear existing documents
            repository.deleteAllDocuments()
            Log.d(TAG, "Cleared existing documents")

            // Insert new documents
            documents.forEach { document ->
                try {
                    repository.insertDocument(document)
                    Log.d(TAG, "Inserted document: ${document.name}")
                } catch (e: Exception) {
                    Log.e(TAG, "Error inserting document ${document.name}: ${e.message}", e)
                }
            }

            Log.d(TAG, "Completed document refresh")
        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing documents: ${e.message}", e)
        } finally {
            _isLoading.value = false
        }
    }
} 