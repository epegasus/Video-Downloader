package dev.pegasus.downloadmanager.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pegasus.downloadmanager.data.entities.DownloadEntity
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
        startDownload(query)
    }

    /* --------------------------- Downloads --------------------------- */

    private val _downloads = MutableLiveData<List<DownloadEntity>>()
    val downloads: LiveData<List<DownloadEntity>> get() = _downloads

    fun fetchDownloads() {
        viewModelScope.launch {
            _downloads.postValue(useCaseDownloads.getDownloads())
        }
    }

    fun startDownload(url: String) {
        viewModelScope.launch {
            useCaseDownloads.startDownload(url)
            fetchDownloads()
        }
    }

    fun pauseDownload(id: Int) {
        viewModelScope.launch {
            useCaseDownloads.pauseDownload(id)
            fetchDownloads()
        }
    }

    fun resumeDownload(id: Int) {
        viewModelScope.launch {
            useCaseDownloads.resumeDownload(id)
            fetchDownloads()
        }
    }

    fun cancelDownload(id: Int) {
        viewModelScope.launch {
            useCaseDownloads.cancelDownload(id)
            fetchDownloads()
        }
    }
}