package dev.pegasus.downloadmanager.data.dataSources

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import dev.pegasus.downloadmanager.utilities.ConstantUtils.TAG

/**
 * Created by: Sohaib Ahmed
 * Date: 3/5/2025
 *
 * Links:
 * - LinkedIn: https://linkedin.com/in/epegasus
 * - GitHub: https://github.com/epegasus
 */

class DataSourceDownloads(private val downloadManager: DownloadManager) {

    private val ongoingDownloads = mutableMapOf<String, Long>()

    fun downloadUrl(videoUrl: String, fileName: String) {
        if (ongoingDownloads.containsKey(videoUrl)) {
            Log.d(TAG, "Download already in progress for: $videoUrl")
            return
        }

        val request = DownloadManager.Request(videoUrl.toUri()).apply {
            setTitle(fileName)
            setDescription("Downloading: $fileName...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        }

        val downloadId = downloadManager.enqueue(request)
        ongoingDownloads[videoUrl] = downloadId
        Log.d(TAG, "Started download: $videoUrl with ID: $downloadId")

        checkDownloadStatus()
    }

    fun checkDownloadStatus() {
        val query = DownloadManager.Query()
        val cursor = downloadManager.query(query)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID))
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            val uri = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_URI))
            val reason = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))
            val reasonMessage = getReasonMessage(reason)

            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    Log.d(TAG, "Download successful: $uri")
                    ongoingDownloads.remove(uri)
                }
                DownloadManager.STATUS_FAILED -> {
                    Log.d(TAG, "Download failed: $uri, Reason: $reasonMessage ($reason)")
                    ongoingDownloads.remove(uri)
                }
                DownloadManager.STATUS_RUNNING -> Log.d(TAG, "Download in progress: $uri")
                DownloadManager.STATUS_PENDING -> Log.d(TAG, "Download pending: $uri")
                DownloadManager.STATUS_PAUSED -> Log.d(TAG, "Download paused: $uri, Reason: $reason")
            }
        }
        cursor.close()
    }

    private fun getReasonMessage(reason: Int): String {
        return when (reason) {
            DownloadManager.ERROR_CANNOT_RESUME -> "Cannot resume download"
            DownloadManager.ERROR_DEVICE_NOT_FOUND -> "Device not found"
            DownloadManager.ERROR_FILE_ALREADY_EXISTS -> "File already exists"
            DownloadManager.ERROR_FILE_ERROR -> "File error"
            DownloadManager.ERROR_HTTP_DATA_ERROR -> "HTTP data error"
            DownloadManager.ERROR_INSUFFICIENT_SPACE -> "Insufficient space"
            DownloadManager.ERROR_TOO_MANY_REDIRECTS -> "Too many redirects"
            DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> "Unhandled HTTP error"
            DownloadManager.ERROR_UNKNOWN -> "Unknown error"
            else -> "Error code: $reason"
        }
    }
}



/*
val request = DownloadManager.Request(videoUrl.toUri())
    .setTitle("Downloading: $fileName...")
    .setDescription(fileName)
    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

val downloadId = downloadManager.enqueue(request)

// Register BroadcastReceiver to get notified when the download is complete
val onComplete = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        if (id == downloadId) {
            Toast.makeText(context, "Download Completed!", Toast.LENGTH_SHORT).show()
        }
    }
}
context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))*/
