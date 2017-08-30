package pers.victor.sail.library

import android.graphics.Bitmap
import java.io.InputStream

/**
 * Created by Victor on 2017/8/27. (ง •̀_•́)ง
 */
internal interface ISailFetcher {
    fun fetchImage(url: String)
    fun decode(inputStream: InputStream, length: Int): Bitmap
}