package pers.victor.sail.library

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
internal object SailPool {
    private val handler = Handler(Looper.getMainLooper())

    private val pool = Executors.newFixedThreadPool(8)

    fun execute(r: Runnable) = pool.execute(r)

    fun main(r: Runnable) = handler.post(r)
}