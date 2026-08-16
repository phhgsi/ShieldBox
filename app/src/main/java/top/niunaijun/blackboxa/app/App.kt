package top.niunaijun.blackboxa.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/4/29 21:21
 */
class App : Application() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private lateinit var mContext: Context

        @JvmStatic
        fun getContext(): Context {
            return mContext
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mContext = base!!
        try {
            AppManager.doAttachBaseContext(base)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            AppManager.doOnCreate(mContext)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}