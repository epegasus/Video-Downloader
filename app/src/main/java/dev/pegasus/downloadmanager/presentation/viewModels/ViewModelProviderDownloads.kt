package dev.pegasus.downloadmanager.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.pegasus.downloadmanager.domain.useCases.UseCaseDownloads
import dev.pegasus.downloadmanager.domain.useCases.UseCaseUrl

/**
 * Created by: Sohaib Ahmed
 * Date: 3/5/2025
 *
 * Links:
 * - LinkedIn: https://linkedin.com/in/epegasus
 * - GitHub: https://github.com/epegasus
 */

@Suppress("UNCHECKED_CAST")
class ViewModelProviderDownloads(private val useCaseUrl: UseCaseUrl, private val useCaseDownloads: UseCaseDownloads) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelDownloads::class.java)) {
            return ViewModelDownloads(useCaseUrl, useCaseDownloads) as T
        }
        return super.create(modelClass)
    }
}