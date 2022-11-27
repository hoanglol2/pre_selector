package com.example.picker_selector_app.wigets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.Interpolator
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.example.picker_selector_app.R
import com.example.picker_selector_app.databinding.LayoutImagePickerSnackbarBinding

class SnackBarView @JvmOverloads constructor(
    context: Context,
    attrsSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrsSet, defStyleAttr) {

    private var isShowing = false
    private lateinit var binding: LayoutImagePickerSnackbarBinding

    companion object {
        private const val ANIM_DURATION = 200
        private val INTERPOLATOR: Interpolator = FastOutLinearInInterpolator()
    }

    init {
        initViews()
    }

    private fun initViews() {
        val layoutInflater = LayoutInflater.from(context)
        binding = LayoutImagePickerSnackbarBinding.inflate(
            layoutInflater, this)
        addView(binding.root)

        if (isInEditMode) {
            return
        }
        binding.apply {
            setBackgroundColor(Color.parseColor("#323232"))
            translationY = height.toFloat()
            alpha = 0f
            isShowing = false
            val horizontalPadding = convertDpToPixels(context, 24f)
            val verticalPadding = convertDpToPixels(context, 14f)
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        }
    }

    private fun setText(textResId: Int) {
        binding.tvSnackBarMessage.setText(textResId)
    }

    private fun setOnActionClickListener(actionText: String, onClickListener: OnClickListener) {
        binding.btnSnackBarAction.apply {
            text = actionText
            setOnClickListener { view -> hide { onClickListener.onClick(view) } }
        }
    }

    fun show(textResId: Int, onClickListener: OnClickListener) {
        setText(textResId)
        setOnActionClickListener(context.getString(R.string.okLabel), onClickListener)
        ViewCompat.animate(this)
            .translationY(0f)
            .setDuration(ANIM_DURATION.toLong())
            .setInterpolator(INTERPOLATOR)
            .alpha(1f)
        isShowing = true
    }

    private fun hide(runnable: Runnable) {
        ViewCompat.animate(this)
            .translationY(height.toFloat())
            .setDuration(ANIM_DURATION.toLong())
            .alpha(0.5f)
            .withEndAction(runnable)
        isShowing = false
    }

    private fun convertDpToPixels(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}
