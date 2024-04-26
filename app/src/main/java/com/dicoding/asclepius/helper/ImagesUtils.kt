package com.dicoding.asclepius.helper
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

object ImagesUtils {
    fun bitmapToImageProxy(bitmap: Bitmap): ImageProxy {
        val width = bitmap.width
        val height = bitmap.height

        // Create planes array with proper size
        val planes = arrayOf<ByteBuffer>(ByteBuffer.allocateDirect(width * height * 4))

        // Fill the planes with bitmap data
        val buffer = planes[0]
        buffer.rewind() // Rewind the buffer before writing
        bitmap.copyPixelsToBuffer(buffer)

        // Create ImageProxy instance using a mock Image object
        val image = ImagesUtils.createImageProxy(width, height, planes)

        return image
    }

    private fun createImageProxy(width: Int, height: Int, planes: Array<ByteBuffer>): ImageProxy {
        val imageProxyClass = ImageProxy::class.java
        val builderMethod = imageProxyClass.getDeclaredMethod(
            "Builder", Int::class.java, Int::class.java, Array<ByteBuffer>::class.java
        )
        val builder = builderMethod.invoke(null, width, height, planes)
        return imageProxyClass.getDeclaredMethod("build").invoke(builder) as ImageProxy
    }
}