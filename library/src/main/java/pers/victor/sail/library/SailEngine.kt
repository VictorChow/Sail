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
    private var skipMemoryCache = false
    private var skipDiskCache = false
    private var url = ""
    private val iCache = SailCache
    private val iFetcher = SailFetcher()
    private var options = SailOptions()

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

    override fun options(options: SailOptions) = apply {
        this.options = options
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
                    SailThreadPool.execute(Runnable {
                        println("磁盘缓存")
                        val cache = iCache.getDiskCache(url)!!
                        if (options.cache == CacheStrategy.ALL || options.cache == CacheStrategy.MEMORY) {
                            iCache.putMemoryCache(url, cache)
                        }
                        SailThreadPool.main(Runnable {
                            checkFade()
                            iv.setImageBitmap(cache)
                        })
                    })
                    return@post
                }
            }

            val w = iv.width
            val h = iv.height
            if (options.width * options.height == 0) {
                when (options.quality) {
                    QualityStrategy.LOW -> options.resize(w, h)
                    QualityStrategy.NORMAL -> options.resize((w * 1.5).toInt(), (h * 1.5).toInt())
                    QualityStrategy.HIGH -> options.resize(w shl 1, h shl 1)
                    QualityStrategy.ORIGINAL -> {
                        options.width = -1
                        options.height = -1
                    }
                }
            }
            if (options.holder != 0) {
                target.setImageResource(options.holder)
            }
            iFetcher.fetch(url, this, options)
        }
    }

    override fun onSuccess(bitmap: Bitmap) {
        checkFade()
        target.setImageBitmap(bitmap)
        SailThreadPool.execute {
            if (options.cache == CacheStrategy.ALL || options.cache == CacheStrategy.MEMORY) {
                iCache.putMemoryCache(url, bitmap)
            }
            if (options.cache == CacheStrategy.ALL || options.cache == CacheStrategy.DISK) {
                iCache.putDiskCache(url, bitmap)
            }
        }
    }

    override fun onFail() {
        if (options.error != 0) {
            target.setImageResource(options.error)
        }
    }

    override fun dispose() {
    }

    private fun checkFade() {
        if (options.fadeIn) {
            target.alpha = 0f
            target.animate().alpha(1f).setDuration(options.duration).setInterpolator(DecelerateInterpolator()).start()
        }
    }
}