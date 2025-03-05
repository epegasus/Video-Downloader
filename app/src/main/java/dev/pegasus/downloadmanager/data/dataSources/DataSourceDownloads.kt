package dev.pegasus.downloadmanager.data.dataSources

import android.app.DownloadManager
import android.os.Environment

/**
 * Created by: Sohaib Ahmed
 * Date: 3/5/2025
 *
 * Links:
 * - LinkedIn: https://linkedin.com/in/epegasus
 * - GitHub: https://github.com/epegasus
 */

class DataSourceDownloads(private val downloadManager: DownloadManager) {

    fun downloadUrl(videoUrl: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading...")
            .setDescription(fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        downloadId = downloadManager.enqueue(request)

        // Register BroadcastReceiver to get notified when the download is complete
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Toast.makeText(context, "Download Completed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}