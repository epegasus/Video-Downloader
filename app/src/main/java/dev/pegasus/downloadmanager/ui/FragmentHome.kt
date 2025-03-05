package dev.pegasus.downloadmanager.ui

import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import dev.pegasus.downloadmanager.R
import dev.pegasus.downloadmanager.base.BaseFragment
import dev.pegasus.downloadmanager.databinding.FragmentHomeBinding
import dev.pegasus.downloadmanager.utilities.extensions.pasteClipboardData
import dev.pegasus.downloadmanager.utilities.extensions.showToast

class FragmentHome : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun onViewCreated() {
        initObservers()

        binding.etInput.addTextChangedListener(textWatcher)
        binding.mbPasteLink.setOnClickListener { onPasteClick() }
        binding.mbStartDownload.setOnClickListener { onStartDownloadClick() }
    }

    private fun initObservers() {
        mainActivity?.viewModelDownloads?.let { viewModel ->
            viewModel.navigateLiveData.observe(viewLifecycleOwner) { isValid ->
                binding.etInput.text?.clear()
                context.showToast(R.string.starting_download)
                findNavController().navigate(R.id.fragmentDownloads)
            }
            viewModel.validUrlLiveData.observe(viewLifecycleOwner) { isValid ->
                context.showToast(R.string.invalid_url)
            }
        }
    }

    private fun onPasteClick() {
        val clipBoardData = context?.pasteClipboardData()
        clipBoardData?.let { text ->
            binding.etInput.setText(text)
            binding.etInput.requestFocus()
        } ?: run {
            context.showToast(R.string.nothing_to_paste)
        }
    }

    private fun onStartDownloadClick() {
        val query = binding.etInput.text.toString()
        mainActivity?.viewModelDownloads?.validateUrl(query)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            binding.mbStartDownload.isVisible = !s.isNullOrEmpty()
        }
    }
}