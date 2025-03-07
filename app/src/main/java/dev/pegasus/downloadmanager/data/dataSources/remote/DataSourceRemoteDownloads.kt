package dev.pegasus.downloadmanager.data.dataSources.remote

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Base64
import android.util.Log
import dev.pegasus.downloadmanager.data.entities.DownloadEntity
import dev.pegasus.downloadmanager.utilities.ConstantUtils.TAG
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.IOException
import androidx.core.net.toUri

/**
 * Created by: Sohaib Ahmed
 * Date: 3/5/2025
 *
 * Links:
 * - LinkedIn: https://linkedin.com/in/epegasus
 * - GitHub: https://github.com/epegasus
 */

class DataSourceRemoteDownloads(private val context: Context) {


    private val client = OkHttpClient()

    fun startDownload(url: String, progressCallback: (progress: Int, speed: String, eta: String) -> Unit): DownloadEntity {
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            val contentLength = response.body?.contentLength() ?: -1L
            if (contentLength <= 0) throw IOException("Invalid content length")

            var downloadedBytes = 0L
            val buffer = ByteArray(1024)
            val inputStream = response.body?.byteStream()
            val startTime = System.currentTimeMillis()

            inputStream?.use { input ->
                while (true) {
                    val read = input.read(buffer)
                    if (read == -1) break
                    downloadedBytes += read

                    val progress = ((downloadedBytes * 100) / contentLength).toInt()
                    val elapsedTime = (System.currentTimeMillis() - startTime) / 1000
                    val speed = if (elapsedTime > 0) "${downloadedBytes / 1024 / elapsedTime} KB/s" else "N/A"

                    val eta = if (downloadedBytes > 0 && elapsedTime > 0) {
                        val remainingBytes = contentLength - downloadedBytes
                        val estimatedTimeLeft = remainingBytes / (downloadedBytes / elapsedTime)
                        "$estimatedTimeLeft sec left"
                    } else {
                        "Calculating..."
                    }

                    progressCallback(progress, speed, eta)
                }
            }

            val metadata = extractMetadata(context, url.toUri())

            return DownloadEntity(
                url = url,
                title = metadata["title"] ?: "Video.mp4",
                status = "completed",
                progress = 100,
                speed = "N/A",
                fileSize = "$contentLength bytes",
                eta = "Completed",
                thumbnailUrl = metadata["thumbnailUrl"] ?: "",
                resolution = metadata["resolution"] ?: "N/A"
            )

        } catch (e: Exception) {
            Log.e(TAG, "Download failed: ${e.message}")
            throw IOException("Download Error: ${e.localizedMessage}")
        }
    }

    fun extractMetadata(context: Context, videoUri: Uri): Map<String, String> {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, videoUri)
            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "Unknown"
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: "0"
            val resolution = "${retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)}x${retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)}"

            val thumbnail = retriever.frameAtTime?.let { bitmap ->
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
            } ?: ""

            mapOf("title" to title, "duration" to duration, "resolution" to resolution, "thumbnailUrl" to thumbnail)

        } catch (e: Exception) {
            Log.e(TAG, "Failed to extract metadata: ${e.message}")
            emptyMap()
        } finally {
            retriever.release()
        }
    }
}