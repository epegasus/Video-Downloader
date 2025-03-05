package dev.pegasus.downloadmanager.data.repository

import dev.pegasus.downloadmanager.data.dataSources.DataSourceDownloads
import dev.pegasus.downloadmanager.domain.repository.RepositoryDownloads

/**
 * Created by: Sohaib Ahmed
 * Date: 3/5/2025
 *
 * Links:
 * - LinkedIn: https://linkedin.com/in/epegasus
 * - GitHub: https://github.com/epegasus
 */

class RepositoryDownloadsImpl(private val dataSourceDownloads: DataSourceDownloads) : RepositoryDownloads {

    override suspend fun downloadUrl(videoUrl: String) {
        dataSourceDownloads.downloadUrl(videoUrl)
    }
}