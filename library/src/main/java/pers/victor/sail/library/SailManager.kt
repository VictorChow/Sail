package pers.victor.sail.library

import android.content.Context
import android.graphics.Bitmap
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
class SailManager private constructor(context: Context) : IManager, IObserver {
    private var skipMemoryCache = false
    private var skipDiskCache = false
    private var fade = false
    private var url = ""
    private var holder = 0
    private var error = 0
    private var width = 0
    private var height = 0
    private lateinit var target: ImageView
    private val iCache = SailCache
    private val iFetcher = SailFetcher()

    companion object {
        fun manage(context: Context) = SailManager(context)
    }

    override fun load(url: String) = apply {
        this.url = url
    }

    override fun skipMemoryCache() = apply {
        skipMemoryCache = true
    }

    override fun skipDiskCache() = apply {
        skipDiskCache = true
    }

    override fun fade() = apply {
        fade = true
    }

    override fun resize(width: Int, height: Int) = apply {
        this.width = width
        this.height = height
    }

    override fun holder(id: Int) = apply {
        this.holder = id
    }

    override fun error(id: Int) = apply {
        this.error = id
    }

    override fun into(iv: ImageView) {
        target = iv
        if (!skipMemoryCache) {
            val cache = iCache.getMemoryCache(url)
            cache?.let {
                iv.setImageBitmap(cache)
                println("内存缓存")
                return
            }
        }
        if (!skipDiskCache) {
            val cache = iCache.getDiskCache(url)
            cache?.let {
                println("磁盘缓存")
                iv.setImageBitmap(cache)
                iCache.putMemoryCache(url, cache)
                return
            }
        }
        println("开始下载")
        println(holder)
        target.setImageResource(holder)
        iFetcher.fetch(url, this)
    }

    override fun onSuccess(bitmap: Bitmap) {
        println("下载成功")
        if (fade) {
            target.alpha = 0f
        }
        target.setImageBitmap(bitmap)
        if (fade) {
            target.animate().alpha(1f).setDuration(500L).setInterpolator(DecelerateInterpolator()).start()
        }
        SailPool.execute(Runnable {
            iCache.putMemoryCache(url, bitmap)
            iCache.putDiskCache(url, bitmap)
        })
    }

    override fun onFail() {
        target.setImageResource(error)
    }

    override fun dispose() {
    }

}