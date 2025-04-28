package com.example.myapplication.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.myapplication.data.Document
import com.example.myapplication.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import android.os.Environment

class DocumentScanService : Service() {
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private lateinit var database: AppDatabase
    private val TAG = "DocumentScanService"

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Starting document scan service")
        serviceScope.launch {
            try {
                scanDocuments()
            } catch (e: Exception) {
                Log.e(TAG, "Error scanning documents: ${e.message}", e)
            }
        }
        return START_STICKY
    }

    private suspend fun scanDocuments() {
        Log.d(TAG, "Starting document scan")
        val documents = mutableListOf<Document>()

        // Scan external storage
        val externalDir = Environment.getExternalStorageDirectory()
        Log.d(TAG, "Scanning external directory: ${externalDir.absolutePath}")
        scanDirectory(externalDir, documents)

        // Scan downloads directory
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Log.d(TAG, "Scanning downloads directory: ${downloadsDir.absolutePath}")
        scanDirectory(downloadsDir, documents)

        // Scan documents directory
        val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        Log.d(TAG, "Scanning documents directory: ${documentsDir.absolutePath}")
        scanDirectory(documentsDir, documents)

        Log.d(TAG, "Found ${documents.size} documents")

        // Insert all found documents into the database
        documents.forEach { document ->
            try {
                database.documentDao().insertDocument(document)
                Log.d(TAG, "Inserted document: ${document.name}")
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting document ${document.name}: ${e.message}", e)
            }
        }
    }

    private fun scanDirectory(directory: File, documents: MutableList<Document>) {
        if (!directory.exists() || !directory.canRead()) {
            Log.w(TAG, "Cannot access directory: ${directory.absolutePath}")
            return
        }

        directory.listFiles()?.forEach { file ->
            try {
                if (file.isDirectory) {
                    scanDirectory(file, documents)
                } else {
                    val mimeType = getMimeType(file.name)
                    if (isDocumentFile(mimeType)) {
                        val document = Document(
                            name = file.name,
                            path = file.absolutePath,
                            type = mimeType ?: "unknown",
                            size = file.length(),
                            lastModified = Date(file.lastModified()),
                            isFavorite = false
                        )
                        documents.add(document)
                        Log.d(TAG, "Found document: ${document.name} (${document.type})")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error scanning file ${file.name}: ${e.message}", e)
            }
        }
    }

    private fun getMimeType(fileName: String): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(fileName)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    private fun isDocumentFile(mimeType: String?): Boolean {
        return mimeType?.let {
            it.startsWith("text/") ||
                    it.startsWith("application/pdf") ||
                    it.startsWith("application/msword") ||
                    it.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml") ||
                    it.startsWith("application/vnd.ms-excel") ||
                    it.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml") ||
                    it.startsWith("application/vnd.ms-powerpoint") ||
                    it.startsWith("application/vnd.openxmlformats-officedocument.presentationml")
        } ?: false
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
} 