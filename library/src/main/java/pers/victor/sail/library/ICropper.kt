package pers.victor.sail.library

import android.graphics.Bitmap

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
internal interface ICropper {
    fun crop(bmp: Bitmap, maxWidth: Int, maxHeight: Int):Bitmap
}