package top.niunaijun.blackboxa.view.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.ProgressBar

/**
 *
 * @Description: loading activity
 * @Author: BlackBox
 * @CreateDate: 2022/3/2 21:49
 */
abstract class LoadingActivity : BaseActivity() {

    private var loadingDialog: Dialog? = null

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = Dialog(this).apply {
                val progressBar = ProgressBar(this@LoadingActivity).apply {
                    isIndeterminate = true
                }
                setContentView(
                    progressBar,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                )
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setOnKeyListener { _, keyCode, _ ->
                    keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE
                }
            }
        }

        if (!isFinishing && loadingDialog?.isShowing == false) {
            loadingDialog?.show()
        }
    }

    fun hideLoading() {
        if (!isFinishing && loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}