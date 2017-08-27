package pers.victor.sail.library

import android.graphics.Bitmap

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
internal interface ICache {
    fun putMemoryCache(url: String, bmp: Bitmap)
    fun putDiskCache(url: String, bmp: Bitmap)
    fun getMemoryCache(url: String): Bitmap?
    fun getDiskCache(url: String): Bitmap?
    fun clear()
}