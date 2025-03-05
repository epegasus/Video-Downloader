package dev.pegasus.downloadmanager.domain.repository

/**
 * Created by: Sohaib Ahmed
 * Date: 3/5/2025
 *
 * Links:
 * - LinkedIn: https://linkedin.com/in/epegasus
 * - GitHub: https://github.com/epegasus
 */

interface RepositoryDownloads {
    suspend fun downloadUrl(videoUrl: String)
}