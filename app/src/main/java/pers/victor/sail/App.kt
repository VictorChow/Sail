package pers.victor.sail

import android.app.Application
import pers.victor.ext.Ext

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Ext.with(this)
    }
}