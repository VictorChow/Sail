package pers.victor.sail.library

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
class SailOptions {
    internal var fadeIn = false
    internal var duration = 500L
    internal var holder = 0
    internal var error = 0
    internal var width = 0
    internal var height = 0
    internal var quality = QualityStrategy.NORMAL
    internal var cache = CacheStrategy.ALL

    fun fadeIn(duration: Long = this.duration) = apply {
        fadeIn = true
    }

    fun resize(width: Int, height: Int) = apply {
        if (width <= 0 || height <= 0) {
            throw IllegalArgumentException("width, height must > 0")
        }
        this.width = width
        this.height = height
    }

    fun holder(id: Int) = apply {
        this.holder = id
    }

    fun error(id: Int) = apply {
        this.error = id
    }

    fun quality(quality: QualityStrategy) = apply {
        this.quality = quality
        width = 0
        height = 0
    }

    fun cache(cache: CacheStrategy) = apply {
        this.cache = cache
    }
}