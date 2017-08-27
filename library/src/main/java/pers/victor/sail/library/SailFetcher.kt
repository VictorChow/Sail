package pers.victor.sail.library

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by Victor on 2017/8/27. (ง •̀_•́)ง
 */
internal class SailFetcher : IFetcher {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var observer: IObserver

    fun fetch(url: String, observer: IObserver) {
        this.observer = observer
        fetchImage(url)
    }

    override fun fetchImage(url: String) {
        val r = Runnable {
            val conn = URL(url).openConnection() as HttpURLConnection
            try {
                conn.connectTimeout = 5000
                conn.requestMethod = "GET"
                if (conn.responseCode == 200) {
                    val bmp = BitmapFactory.decodeStream(conn.inputStream)
                    handler.post {
                        observer.onSuccess(bmp)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    observer.onFail()
                }
            } finally {
                conn.disconnect()
            }
        }
        SailPool.execute(r)
    }

}