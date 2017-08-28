package pers.victor.sail.library

import android.content.Context
import android.graphics.Bitmap
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
class SailEngine private constructor(context: Context) : ISailEngine, ISailObserver {
    private lateinit var target: ImageView
    private var iCropper: ICropper = SailCropper
    private var skipMemoryCache = false
    private var skipDiskCache = false
    private var fadeIn = false
    private var url = ""
    private var holder = 0
    private var error = 0
    private var width = 0
    private var height = 0
    private val iCache = SailCache
    private val iFetcher = SailFetcher()

    companion object {
        fun manage(context: Context) = SailEngine(context)
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

    override fun fadeIn() = apply {
        fadeIn = true
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
        target.post {
            if (!skipMemoryCache) {
                val cache = iCache.getMemoryCache(url)
                cache?.let {
                    checkFade()
                    iv.setImageBitmap(cache)
                    println("内存缓存")
                    return@post
                }
            }
            if (!skipDiskCache) {
                val hasDiskCache = iCache.hasDiskCache(url)
                if (hasDiskCache) {
                    SailPool.execute(Runnable {
                        println("磁盘缓存")
                        val cache = iCache.getDiskCache(url)!!
                        iCache.putMemoryCache(url, cache)
                        SailPool.main(Runnable {
                            checkFade()
                            iv.setImageBitmap(cache)
                        })
                    })
                    return@post
                }
            }
            target.setImageResource(holder)
            iFetcher.fetch(url, this)
        }
    }

    override fun onSuccess(bitmap: Bitmap) {
        SailPool.execute(Runnable {
            val bmp = iCropper.crop(bitmap, (target.width * 1.5).toInt(), (target.height * 1.5).toInt())
            println("old: ${bitmap.width}x${bitmap.height}")
            println("new: ${bmp.width}x${bmp.height}")
            SailPool.main(Runnable {
                checkFade()
                target.setImageBitmap(bmp)
            })
            iCache.putMemoryCache(url, bmp)
            iCache.putDiskCache(url, bmp)
        })
    }

    override fun onFail() {
        target.setImageResource(error)
    }

    override fun dispose() {
    }

    private fun checkFade() {
        if (fadeIn) {
            target.alpha = 0f
            target.animate().alpha(1f).setDuration(500L).setInterpolator(DecelerateInterpolator()).start()
        }
    }
}