package pers.victor.sail.library

import android.support.annotation.DrawableRes
import android.widget.ImageView

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
internal interface ISailEngine {
    fun load(url: String): ISailEngine
    fun skipMemoryCache(): ISailEngine
    fun skipDiskCache(): ISailEngine
    fun fadeIn(): ISailEngine
    fun resize(width: Int, height: Int): ISailEngine
    fun holder(@DrawableRes id: Int): ISailEngine
    fun error(@DrawableRes id: Int): ISailEngine
    fun into(iv: ImageView)
    fun dispose()
}