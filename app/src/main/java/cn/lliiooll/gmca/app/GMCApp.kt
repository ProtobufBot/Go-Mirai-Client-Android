package cn.lliiooll.gmca.app

import android.app.Application

class GMCApp : Application() {


    override fun onCreate() {
        INSTANCE = this
        super.onCreate()
    }

    companion object {
        private var INSTANCE: GMCApp? = null

        @JvmStatic
        fun getApplication(): GMCApp? {
            return INSTANCE
        }
    }
}