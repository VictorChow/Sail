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
    private var url = ""
    private val iCache = SailCache
    private val iFetcher = SailFetcher()
    private var options: SailOptions? = null

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
                    SailPool.execute(Runnable {
                        println("磁盘缓存")
                        val cache = iCache.getDiskCache(url)!!
                        if (options != null && options!!.cache == CacheStrategy.ALL || options!!.cache == CacheStrategy.MEMORY) {
                            iCache.putMemoryCache(url, cache)
                        }
                        SailPool.main(Runnable {
                            checkFade()
                            iv.setImageBitmap(cache)
                        })
                    })
                    return@post
                }
            }
            options?.let { target.setImageResource(it.holder) }
            iFetcher.fetch(url, this)
        }
    }

    override fun onSuccess(bitmap: Bitmap) {
        SailPool.execute(Runnable {
            val bmp = if (options != null) {
                when (options!!.quality) {
                    QualityStrategy.LOW -> iCropper.crop(bitmap, target.width, target.height)
                    QualityStrategy.NORMAL -> iCropper.crop(bitmap, (target.width * 1.5).toInt(), (target.height * 1.5).toInt())
                    QualityStrategy.HIGH -> iCropper.crop(bitmap, target.width * 2, target.height * 2)
                    QualityStrategy.ORIGINAL -> bitmap
                    QualityStrategy.NONE -> iCropper.crop(bitmap, options!!.width, options!!.height)
                }
            } else {
                iCropper.crop(bitmap, (target.width * 1.5).toInt(), (target.height * 1.5).toInt())
            }
            println("old: ${bitmap.width}x${bitmap.height}")
            println("new: ${bmp.width}x${bmp.height}")
            SailPool.main(Runnable {
                checkFade()
                target.setImageBitmap(bmp)
            })
            if (options != null && options!!.cache == CacheStrategy.ALL || options!!.cache == CacheStrategy.MEMORY) {
                iCache.putMemoryCache(url, bmp)
            }
            if (options != null && options!!.cache == CacheStrategy.ALL || options!!.cache == CacheStrategy.DISK) {
                iCache.putDiskCache(url, bmp)
            }
        })
    }

    override fun onFail() {
        options?.let { target.setImageResource(it.error) }
    }

    override fun dispose() {
    }

    private fun checkFade() {
        if (options != null && options!!.fadeIn) {
            target.alpha = 0f
            target.animate().alpha(1f).setDuration(options!!.duration).setInterpolator(DecelerateInterpolator()).start()
        }
    }
}