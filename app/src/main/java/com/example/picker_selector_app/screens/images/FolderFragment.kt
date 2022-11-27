package com.example.picker_selector_app.screens.images

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.picker_selector_app.R
import com.example.picker_selector_app.adapters.ImageFolderAdapter
import com.example.picker_selector_app.consts.CallbackStatus
import com.example.picker_selector_app.databinding.FragmentFolderBinding
import com.example.picker_selector_app.models.GridCount
import com.example.picker_selector_app.models.Result
import com.example.picker_selector_app.screens.common.BaseFragment
import com.example.picker_selector_app.screens.images.ImagePickerScreen.Companion.GRID_COUNT
import com.example.picker_selector_app.utils.GridSpacingItemDecoration
import com.example.picker_selector_app.utils.helper.ImageHelper
import com.example.picker_selector_app.utils.helper.LayoutManagerHelper
import com.example.picker_selector_app.viewmodels.ImagePickerViewModel

class FolderFragment : BaseFragment() {

    private var binding: FragmentFolderBinding? = null
    private val folderAdapter by lazy { ImageFolderAdapter() }
    private lateinit var gridCount: GridCount
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var itemDecoration: GridSpacingItemDecoration
    private val imagePickerShareVM: ImagePickerViewModel by navGraphViewModels(R.id.nav_graph_image) {
        ImagePickerViewModel.factory()
    }

    companion object {
        @JvmStatic
        fun newInstance(gridCount: GridCount): FolderFragment {
            val fragment = FolderFragment()
            val args = Bundle()
            args.putParcelable(GRID_COUNT, gridCount)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridCount = arguments?.getParcelable(GRID_COUNT)
            ?: GridCount(0, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFolderBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupRecyclerview()
        initObserver()
        initListener()
    }

    private fun initViews() {
        binding?.apply {
            val config = imagePickerShareVM.getConfig()
            root.setBackgroundColor(Color.parseColor(config.backgroundColor))
            prgIndicator.setIndicatorColor(Color.parseColor(config.progressIndicatorColor))
        }
    }

    private fun setupRecyclerview() {
        binding?.apply {
            gridLayoutManager = LayoutManagerHelper.newInstance(safeContext, gridCount)
            itemDecoration = getGridSpacing()
            rvFolder.apply {
                setHasFixedSize(true)
                layoutManager = gridLayoutManager
                addItemDecoration(itemDecoration)
                adapter = folderAdapter
            }
        }
    }

    private fun initObserver() {
        imagePickerShareVM.imageResultLiveData.observe(viewLifecycleOwner) {
            handleResult(it)
        }
    }

    private fun handleResult(result: Result) {
        binding?.apply {
            val isHaveResult = result.status is CallbackStatus.SUCCESS && result.images.isNotEmpty()
            val isEmptyData = result.status is CallbackStatus.SUCCESS && result.images.isEmpty()
            if (isHaveResult) {
                val folders = ImageHelper.folderListFromImages(result.images)
                folderAdapter.setList(folders)
            }
            rvFolder.isVisible = isHaveResult
            tvEmptyText.isVisible = isEmptyData
            prgIndicator.isVisible = result.status is CallbackStatus.FETCHING
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding?.apply {
            rvFolder.removeItemDecoration(itemDecoration)
            val newSpanCount =
                LayoutManagerHelper.getSpanCountForCurrentConfiguration(safeContext, gridCount)
            itemDecoration = getGridSpacing()
            gridLayoutManager.spanCount = newSpanCount
            rvFolder.addItemDecoration(itemDecoration)
        }
    }

    private fun getGridSpacing() = GridSpacingItemDecoration(
        gridLayoutManager.spanCount,
        resources.getDimension(R.dimen.dp1).toInt()
    )

    private fun initListener() {
        folderAdapter.setOnItemClickListener {
            (parentFragment as? ImagePickerScreen)?.onFolderClick(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}