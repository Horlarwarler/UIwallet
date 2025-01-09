package com.crezent.finalyearproject.core.data.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64

class JavaBase64Util : Base64Util {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun encodeToString(byteArray: ByteArray): String {
        return Base64.getEncoder().encodeToString(byteArray)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun decodeToByteArray(enCodedString: String): ByteArray {
        return Base64.getDecoder().decode(enCodedString)
    }
}