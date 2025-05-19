package com.bansarichothani.kotlindemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bansarichothani.kotlindemo.databinding.ActivityLockscreenBinding

class LockscreenActivity : AppCompatActivity() {

    private var binding: ActivityLockscreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLockscreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            btnSet.setOnClickListener {
                val intent = Intent(this@LockscreenActivity, WallpaperService::class.java)
                ContextCompat.startForegroundService(this@LockscreenActivity, intent)
            }

            btnStop.setOnClickListener {
                val intent = Intent(this@LockscreenActivity, WallpaperService::class.java).apply {
                    action = WallpaperService.ACTION_STOP
                }
                stopService(intent)
            }

            btnAddImage.setOnClickListener {
                val intent = Intent(this@LockscreenActivity, WallpaperService::class.java).apply {
//                    action = WallpaperService.ACTION_ADD_IMAGE
//                    putExtra(WallpaperService.EXTRA_IMAGE_RES_ID, R.drawable.wal8)
                }
                ContextCompat.startForegroundService(this@LockscreenActivity, intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
