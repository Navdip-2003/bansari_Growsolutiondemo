package com.bansarichothani.kotlindemo

import android.app.*
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import java.io.IOException

class WallpaperService : Service() {

    private val wallpapersResIds = listOf(
        R.drawable.wal1, R.drawable.wal2, R.drawable.wal3,
        R.drawable.wal4, R.drawable.wal5, R.drawable.wal6, R.drawable.wal7,
        R.drawable.wal8, R.drawable.wal9, R.drawable.wal10, R.drawable.wal11,
        R.drawable.wal12, R.drawable.wal13, R.drawable.wal14,
        R.drawable.wal15, R.drawable.wal16, R.drawable.wal17, R.drawable.wal18,
        R.drawable.wal19, R.drawable.wal20, R.drawable.wal21, R.drawable.wal22,
        R.drawable.wal23, R.drawable.wal24, R.drawable.wal25, R.drawable.wal26,
        R.drawable.wal27, R.drawable.wal28, R.drawable.wal29, R.drawable.wal30,
        R.drawable.wal31, R.drawable.wal32
    )

    private val bitmapCache = mutableListOf<Bitmap?>()
    private var index = 0
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var wallpaperJob: Job? = null

    companion object {
        const val ACTION_STOP = "com.bansarichothani.kotlindemo.action.STOP"
        const val NOTIFICATION_CHANNEL_ID = "wallpaper_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        preloadBitmaps()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopSelf()
                return START_NOT_STICKY
            }
        }

        startForeground(NOTIFICATION_ID, createNotification())

        if (wallpaperJob?.isActive != true) {
            wallpaperJob = serviceScope.launch {
                while (isActive) {
                    changeWallpaper()
                    delay(1_000L) // Every 10 seconds
                }
            }
        }

        return START_STICKY
    }

    private fun preloadBitmaps() {
        Log.d("WallpaperService", "Preloading bitmaps...")
        for (resId in wallpapersResIds) {
            val bitmap = decodeSampledBitmapFromResource(resId, 1080, 1920)
            bitmapCache.add(bitmap)
        }
    }

    private fun changeWallpaper() {
        if (bitmapCache.isEmpty()) {
            Log.e("WallpaperService", "Bitmap cache is empty")
            return
        }

        val bitmap = bitmapCache[index]
        if (bitmap == null) {
            Log.e("WallpaperService", "Bitmap at index $index is null")
            return
        }

        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }
            Log.d("WallpaperService", "Wallpaper changed to index: $index")
        } catch (e: IOException) {
            Log.e("WallpaperService", "Error setting wallpaper", e)
        }

        index = (index + 1) % bitmapCache.size
    }

    private fun decodeSampledBitmapFromResource(resId: Int, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(resources, resId, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false

        return try {
            BitmapFactory.decodeResource(resources, resId, options)
        } catch (e: Exception) {
            Log.e("WallpaperService", "Failed to decode bitmap", e)
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Wallpaper Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val stopIntent = Intent(this, WallpaperService::class.java).apply {
            action = ACTION_STOP
        }

        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Wallpaper Service Running")
            .setContentText("Changing your wallpaper every 10 seconds")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(R.drawable.ic_launcher_foreground, "Stop", stopPendingIntent)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        wallpaperJob?.cancel()
        serviceScope.cancel()
        Log.d("WallpaperService", "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
