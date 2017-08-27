package pers.victor.sail.library

import java.util.concurrent.Executors

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
internal object SailPool {
    private val pool = Executors.newFixedThreadPool(8)

    fun execute(r: Runnable) = pool.execute(r)
}