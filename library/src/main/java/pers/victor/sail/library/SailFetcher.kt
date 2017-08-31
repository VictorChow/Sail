package pers.victor.sail.library

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by Victor on 2017/8/27. (ง •̀_•́)ง
 */
internal class SailFetcher : ISailFetcher {
    private lateinit var observer: ISailObserver
    private lateinit var options: SailOptions

    fun fetch(url: String, observer: ISailObserver, options: SailOptions) {
        this.observer = observer
        this.options = options
        fetchImage(url)
    }

    override fun fetchImage(url: String) {
        SailThreadPool.execute {
            val conn = URL(url).openConnection() as HttpURLConnection
            try {
                conn.connectTimeout = 5000
                conn.requestMethod = "GET"
                if (conn.responseCode == 200) {
                    val bmp = decode(conn.inputStream, conn.contentLength)
                    SailThreadPool.main { observer.onSuccess(bmp) }
                } else {
                    SailThreadPool.main { observer.onFail() }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                SailThreadPool.main { observer.onFail() }
            } finally {
                conn.disconnect()
            }
        }
    }

    override fun decode(inputStream: InputStream, length: Int): Bitmap {
        val buffer = BufferedInputStream(inputStream)
        buffer.mark(length)
        val opt = BitmapFactory.Options()
        opt.inJustDecodeBounds = true
        BitmapFactory.decodeStream(buffer, null, opt)
        val pW = (opt.outWidth.toFloat() / options.width + 0.5f).toInt()
        val pH = (opt.outHeight.toFloat() / options.height + 0.5f).toInt()
        opt.inJustDecodeBounds = false
        opt.inSampleSize = if (pW > pH) pW else pH
        buffer.reset()
        return BitmapFactory.decodeStream(buffer, null, opt)
    }
}