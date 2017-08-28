package pers.victor.sail.library

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * Created by Victor on 2017/8/28. (ง •̀_•́)ง
 */
internal object SailCropper : ICropper {

    override fun crop(bmp: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bmp.width
        val height = bmp.height
        return if (height >= width) {
            val p = height.toFloat() / width
            if (height > maxHeight) {
                resizeImage(bmp, maxHeight / p, maxHeight.toFloat())
            } else {
                resizeImage(bmp, height.toFloat() / p, height.toFloat())
            }
        } else {
            val p = width.toFloat() / height
            if (width > maxWidth) {
                resizeImage(bmp, maxWidth.toFloat(), maxWidth / p)
            } else {
                resizeImage(bmp, width.toFloat(), width.toFloat() / p)
            }
        }
    }

    private fun resizeImage(bmp: Bitmap, w: Float, h: Float): Bitmap {
        val width = bmp.width
        val height = bmp.height
        val scaleWidth = w / width
        val scaleHeight = h / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        if (width > 0 && height > 0) {
            return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true)
        }
        return bmp
    }


}