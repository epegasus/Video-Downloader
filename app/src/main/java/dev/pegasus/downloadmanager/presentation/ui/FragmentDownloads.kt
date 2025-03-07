package dev.pegasus.downloadmanager.presentation.ui

import android.util.Log
import dev.pegasus.downloadmanager.base.BaseFragment
import dev.pegasus.downloadmanager.data.entities.DownloadEntity
import dev.pegasus.downloadmanager.databinding.FragmentDownloadsBinding
import dev.pegasus.downloadmanager.presentation.adapters.AdapterDownloads
import dev.pegasus.downloadmanager.utilities.ConstantUtils.TAG

class FragmentDownloads : BaseFragment<FragmentDownloadsBinding>(FragmentDownloadsBinding::inflate) {

    override fun onViewCreated() {
        initObservers()
    }

    private fun initObservers() {
        mainActivity?.viewModelDownloads?.fetchDownloads()

        mainActivity?.viewModelDownloads?.downloads?.observe(viewLifecycleOwner) { list ->
            initRecyclerView(list)
            list.forEach {
                Log.d(TAG, "initObservers: DownloadEntity: $it")
            }
        }
    }

    private fun initRecyclerView(list: List<DownloadEntity>) {
        val adapter = AdapterDownloads(list)
        binding.rcvList.adapter = adapter
    }
}