package pers.victor.sail.library

import android.content.Context

/**
 * Created by Victor on 2017/8/27. (ง •̀_•́)ง
 */
object Sail {
    fun with(ctx: Context) = SailEngine.manage(ctx)

    fun clearCache() = SailCache.clear()
}