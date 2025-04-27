package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bansi.myapplication.R
import com.bansi.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.service.DocumentScanService
import com.example.myapplication.ui.fragments.AllFilesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            Log.d(TAG, "All permissions granted")
            checkAndRequestPermissions()
        } else {
            Log.e(TAG, "Some permissions were not granted")
            Toast.makeText(this, "Storage permissions are required", Toast.LENGTH_LONG).show()
        }
    }

    private val requestManageStoragePermission = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Environment.isExternalStorageManager()) {
            Log.d(TAG, "MANAGE_EXTERNAL_STORAGE permission granted")
            checkAndRequestPermissions()
        } else {
            Log.e(TAG, "MANAGE_EXTERNAL_STORAGE permission not granted")
            Toast.makeText(this, "Storage permission is required", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startDocumentScanService()
        setupFragment(savedInstanceState)
        setupBottomNavigation()
        checkAndRequestPermissions()
    }

    private fun setupFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AllFilesFragment())
                .commit()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? AllFilesFragment
            when (item.itemId) {
                R.id.nav_all_files -> {
                    fragment?.showAllFiles()
                    true
                }
                R.id.nav_recent -> {
                    fragment?.showRecentFiles()
                    true
                }
                R.id.nav_favorites -> {
                    fragment?.showFavoriteFiles()
                    true
                }
                else -> false
            }
        }
    }

    private fun checkAndRequestPermissions() {
        Log.d(TAG, "Checking permissions")
        when {
            // For Android 11 and above, use MANAGE_EXTERNAL_STORAGE
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                if (Environment.isExternalStorageManager()) {
                    Log.d(TAG, "Already have MANAGE_EXTERNAL_STORAGE permission")
                    return
                }
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse("package:${applicationContext.packageName}")
                    requestManageStoragePermission.launch(intent)
                } catch (e: Exception) {
                    Log.e(TAG, "Error requesting MANAGE_EXTERNAL_STORAGE permission", e)
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    requestManageStoragePermission.launch(intent)
                }
            }
            // For Android 10 and below, use READ_EXTERNAL_STORAGE
            else -> {
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        Log.d(TAG, "Already have READ_EXTERNAL_STORAGE permission")
                        return
                    }
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) -> {
                        Log.d(TAG, "Showing permission rationale")
                        Toast.makeText(
                            this,
                            "Storage permission is required to scan documents",
                            Toast.LENGTH_LONG
                        ).show()
                        requestPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                    }
                    else -> {
                        Log.d(TAG, "Requesting storage permissions")
                        requestPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                    }
                }
            }
        }
    }

    private fun startDocumentScanService() {
        startService(Intent(this, DocumentScanService::class.java))
    }
} 