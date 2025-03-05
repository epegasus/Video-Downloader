package dev.pegasus.downloadmanager.domain.useCases

import dev.pegasus.downloadmanager.data.repository.RepositoryDownloadsImpl

/**
 * Created by: Sohaib Ahmed
 * Date: 3/5/2025
 *
 * Links:
 * - LinkedIn: https://linkedin.com/in/epegasus
 * - GitHub: https://github.com/epegasus
 */

class UseCaseDownloads(private val repository: RepositoryDownloadsImpl) {

    suspend fun downloadUrl(videoUrl: String) {
        repository.downloadUrl(videoUrl)
    }
}