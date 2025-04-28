package com.example.myapplication.data

import android.content.Context
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Date

class DocumentLoader(private val context: Context) {
    private val TAG = "DocumentLoader"

    suspend fun loadDocuments(): List<Document> = withContext(Dispatchers.IO) {
        val documents = mutableListOf<Document>()
        try {
            // Check if external storage is available
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.e(TAG, "External storage is not mounted")
                return@withContext documents
            }

            // Scan external storage root
            val externalDir = Environment.getExternalStorageDirectory()
            Log.d(TAG, "External storage path: ${externalDir.absolutePath}")
            Log.d(TAG, "External storage exists: ${externalDir.exists()}")
            Log.d(TAG, "External storage can read: ${externalDir.canRead()}")
            scanDirectory(externalDir, documents)

            // Scan downloads directory
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            Log.d(TAG, "Downloads path: ${downloadsDir.absolutePath}")
            Log.d(TAG, "Downloads exists: ${downloadsDir.exists()}")
            Log.d(TAG, "Downloads can read: ${downloadsDir.canRead()}")
            scanDirectory(downloadsDir, documents)

            // Scan documents directory
            val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            Log.d(TAG, "Documents path: ${documentsDir.absolutePath}")
            Log.d(TAG, "Documents exists: ${documentsDir.exists()}")
            Log.d(TAG, "Documents can read: ${documentsDir.canRead()}")
            scanDirectory(documentsDir, documents)

            // Scan app's external files directory
            val appExternalDir = context.getExternalFilesDir(null)
            Log.d(TAG, "App external path: ${appExternalDir?.absolutePath}")
            Log.d(TAG, "App external exists: ${appExternalDir?.exists()}")
            Log.d(TAG, "App external can read: ${appExternalDir?.canRead()}")
            appExternalDir?.let { scanDirectory(it, documents) }

            Log.d(TAG, "Found ${documents.size} documents")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading documents: ${e.message}", e)
        }
        documents
    }

    private fun scanDirectory(directory: File, documents: MutableList<Document>) {
        if (!directory.exists() || !directory.canRead()) {
            Log.w(TAG, "Cannot access directory: ${directory.absolutePath}")
            return
        }

        Log.d(TAG, "Scanning directory: ${directory.absolutePath}")
        val files = directory.listFiles()
        Log.d(TAG, "Found ${files?.size ?: 0} files in directory")

        files?.forEach { file ->
            try {
                if (file.isDirectory) {
                    scanDirectory(file, documents)
                } else {
                    val mimeType = getMimeType(file.name)
                    Log.d(TAG, "File: ${file.name}, MimeType: $mimeType")
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
        Log.d(TAG, "File: $fileName, Extension: $extension")
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    private fun isDocumentFile(mimeType: String?): Boolean {
        val isDoc = mimeType?.let {
            it.startsWith("text/") ||
                    it.startsWith("application/pdf") ||
                    it.startsWith("application/msword") ||
                    it.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml") ||
                    it.startsWith("application/vnd.ms-excel") ||
                    it.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml") ||
                    it.startsWith("application/vnd.ms-powerpoint") ||
                    it.startsWith("application/vnd.openxmlformats-officedocument.presentationml")
        } ?: false
        Log.d(TAG, "MimeType: $mimeType, IsDocument: $isDoc")
        return isDoc
    }
} 