package pers.victor.sail.library

import android.support.annotation.DrawableRes
import android.widget.ImageView

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
internal interface IManager {
    fun load(url: String): IManager
    fun skipMemoryCache(): IManager
    fun skipDiskCache(): IManager
    fun fade(): IManager
    fun resize(width: Int, height: Int): IManager
    fun holder(@DrawableRes id: Int): IManager
    fun error(@DrawableRes id: Int): IManager
    fun into(iv: ImageView)
    fun dispose()
}