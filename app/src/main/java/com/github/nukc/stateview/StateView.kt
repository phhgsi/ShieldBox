package com.github.nukc.stateview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import top.niunaijun.blackboxa.R

class StateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mEmptyView: View? = null
    private var mLoadingView: View? = null
    private var mContentView: View? = null
    var emptyResource: Int = R.layout.base_empty

    init {
        // Setup initial loading view
        val progressBar = ProgressBar(context).apply {
            isIndeterminate = true
        }
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = android.view.Gravity.CENTER
        }
        mLoadingView = progressBar
        addView(mLoadingView, lp)
        mLoadingView?.visibility = View.GONE
    }

    fun showLoading(): View {
        visibility = View.VISIBLE
        mLoadingView?.visibility = View.VISIBLE
        mEmptyView?.visibility = View.GONE
        mContentView?.visibility = View.GONE
        return mLoadingView!!
    }

    fun showEmpty(): View {
        visibility = View.VISIBLE
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(context).inflate(emptyResource, this, false)
            addView(mEmptyView)
        }
        mEmptyView?.visibility = View.VISIBLE
        mLoadingView?.visibility = View.GONE
        mContentView?.visibility = View.GONE
        return mEmptyView!!
    }

    fun showContent() {
        visibility = View.GONE
        mLoadingView?.visibility = View.GONE
        mEmptyView?.visibility = View.GONE
        mContentView?.visibility = View.VISIBLE
    }

    fun showRetry(): View {
        return showEmpty()
    }
}
