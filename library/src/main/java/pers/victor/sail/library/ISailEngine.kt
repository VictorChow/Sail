package pers.victor.sail.library

import android.widget.ImageView

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
internal interface ISailEngine {
    fun load(url: String): ISailEngine
    fun skipMemoryCache(): ISailEngine
    fun skipDiskCache(): ISailEngine
    fun options(options: SailOptions): ISailEngine
    fun into(iv: ImageView)
    fun dispose()
}