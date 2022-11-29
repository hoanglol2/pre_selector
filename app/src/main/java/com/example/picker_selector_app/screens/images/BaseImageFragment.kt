package com.example.picker_selector_app.screens.images

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.picker_selector_app.R
import com.example.picker_selector_app.databinding.FragmentBaseImageFolderBinding
import com.example.picker_selector_app.models.GridCount
import com.example.picker_selector_app.models.ImagePickerConfig
import com.example.picker_selector_app.models.Result
import com.example.picker_selector_app.screens.common.BaseFragment
import com.example.picker_selector_app.utils.GridSpacingItemDecoration
import com.example.picker_selector_app.utils.helper.LayoutManagerHelper

abstract class BaseImageFragment : BaseFragment() {

    protected var binding: FragmentBaseImageFolderBinding? = null
    private lateinit var gridCount: GridCount
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var itemDecoration: GridSpacingItemDecoration

    abstract fun handleResult(result: Result)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridCount = arguments?.getParcelable(ImagePickerScreen.GRID_COUNT)
            ?: GridCount(1, 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseImageFolderBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
    }

    fun setupViewsWithConfig(config: ImagePickerConfig) {
        binding?.apply {
            root.setBackgroundColor(Color.parseColor(config.backgroundColor))
            prgIndicator.setIndicatorColor(Color.parseColor(config.progressIndicatorColor))
        }
    }

    open fun setupRecyclerview() {
        binding?.apply {
            gridLayoutManager = LayoutManagerHelper.newInstance(safeContext, gridCount)
            itemDecoration = getGridSpacing()
            rvImage.apply {
                setHasFixedSize(true)
                layoutManager = gridLayoutManager
                addItemDecoration(itemDecoration)
            }
        }
    }

    private fun getGridSpacing() = GridSpacingItemDecoration(
        gridLayoutManager.spanCount,
        resources.getDimension(R.dimen.dp1).toInt()
    )

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupUIWhenConfigChange()
    }

    open fun setupUIWhenConfigChange() {
        binding?.apply {
            rvImage.removeItemDecoration(itemDecoration)
            val newSpanCount =
                LayoutManagerHelper.getSpanCountForCurrentConfiguration(safeContext, gridCount)
            itemDecoration = getGridSpacing()
            gridLayoutManager.spanCount = newSpanCount
            rvImage.addItemDecoration(itemDecoration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}