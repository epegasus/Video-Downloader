package dev.pegasus.downloadmanager

import android.app.DownloadManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import dev.pegasus.downloadmanager.data.dataSources.DataSourceDownloads
import dev.pegasus.downloadmanager.data.repository.RepositoryDownloadsImpl
import dev.pegasus.downloadmanager.databinding.ActivityMainBinding
import dev.pegasus.downloadmanager.domain.useCases.UseCaseDownloads
import dev.pegasus.downloadmanager.domain.useCases.UseCaseUrl
import dev.pegasus.downloadmanager.viewModels.ViewModelDownloads
import dev.pegasus.downloadmanager.viewModels.ViewModelProviderDownloads

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // MVVM
    private val downloadManager by lazy { getSystemService(DOWNLOAD_SERVICE) as DownloadManager }
    private val dataSourceDownloads by lazy { DataSourceDownloads(downloadManager) }
    private val repositoryDownloadsImpl by lazy { RepositoryDownloadsImpl(dataSourceDownloads) }
    private val useCaseDownloads by lazy { UseCaseDownloads(repositoryDownloadsImpl) }
    private val useCaseUrl by lazy { UseCaseUrl() }
    val viewModelDownloads by viewModels<ViewModelDownloads> { ViewModelProviderDownloads(useCaseUrl, useCaseDownloads) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fullScreen()
        initNavController()
    }

    private fun fullScreen() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initNavController() {
        val navController = (supportFragmentManager.findFragmentById(binding.fcvContainer.id) as NavHostFragment).navController

        binding.bnvBar.setupWithNavController(navController)
        binding.bnvBar.setOnItemSelectedListener { it.onNavDestinationSelected(navController) }
    }
}