package pers.victor.sail.library

import android.graphics.Bitmap

/**
 * Created by Victor on 2017/8/27. (ง •̀_•́)ง
 */
internal interface IObserver {
    fun onSuccess(bitmap: Bitmap)
    fun onFail()
}