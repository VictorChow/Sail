package pers.victor.sail.library

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.LruCache
import pers.victor.ext.md5
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Victor on 2017/8/27. (ง •̀_•́)ง
 */
internal object SailCache : ISailCache {
    private val cacheMemory = LruCache<String, Bitmap>(8 * 1024 * 1024)
    private val cacheDiskPath = Environment.getExternalStorageDirectory().absolutePath + "/1/"

    override fun putMemoryCache(url: String, bmp: Bitmap) {
        if (cacheMemory.get(url.md5()) == null) {
            cacheMemory.put(url.md5(), bmp)
        }
    }

    override fun putDiskCache(url: String, bmp: Bitmap) {
        checkDiskCacheFolder()
        val f = File("$cacheDiskPath/${url.md5()}")
        if (f.exists()) {
            return
        }
        f.createNewFile()
        val stream = FileOutputStream(f)
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
    }

    override fun getMemoryCache(url: String): Bitmap? = cacheMemory.get(url.md5())

    override fun getDiskCache(url: String): Bitmap? {
        checkDiskCacheFolder()
        return BitmapFactory.decodeFile("$cacheDiskPath/${url.md5()}")
    }

    private fun checkDiskCacheFolder() {
        val f = File(cacheDiskPath)
        if (!f.exists()) {
            f.mkdirs()
        }
    }

    override fun hasDiskCache(url: String) = File("$cacheDiskPath/${url.md5()}").exists()

    override fun clear() {
        cacheMemory.evictAll()
        val f = File(cacheDiskPath)
        if (f.exists()) {
            f.listFiles().forEach { it.delete() }
            f.delete()
        }
    }
}