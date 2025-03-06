package dev.pegasus.downloadmanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pegasus.downloadmanager.domain.useCases.UseCaseDownloads
import dev.pegasus.downloadmanager.domain.useCases.UseCaseUrl
import dev.pegasus.downloadmanager.utilities.SingleLiveEvent
import kotlinx.coroutines.launch

/**
 * Created by: Sohaib Ahmed
 * Date: 3/5/2025
 *
 * Links:
 * - LinkedIn: https://linkedin.com/in/epegasus
 * - GitHub: https://github.com/epegasus
 */

class ViewModelDownloads(private val useCaseUrl: UseCaseUrl, private val useCaseDownloads: UseCaseDownloads) : ViewModel() {

    private val _inValidUrlLiveData = SingleLiveEvent<Unit>()
    val inValidUrlLiveData: LiveData<Unit> get() = _inValidUrlLiveData

    private val _validUrlLiveData = SingleLiveEvent<Unit>()
    val validUrlLiveData: LiveData<Unit> get() = _validUrlLiveData

    fun validateUrl(query: String) = viewModelScope.launch {
        if (useCaseUrl.validateUrl(query).not()) {
            _inValidUrlLiveData.value = Unit
            return@launch
        }
        _validUrlLiveData.value = Unit
        downloadUrl(query)
    }

    private suspend fun downloadUrl(videoUrl: String) {
        useCaseDownloads.downloadUrl(videoUrl)
    }
}