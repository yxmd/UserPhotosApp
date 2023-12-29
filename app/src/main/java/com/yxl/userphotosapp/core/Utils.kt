package com.yxl.userphotosapp.core

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


object Utils {

    fun encodeToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    val currentTimestamp = Date().time / 1000

    fun convertToDateTime(timestamp: Long, hasTime: Boolean) : String{
        val date = Date(timestamp * 1000)
        val sdf = when(hasTime){
            true -> {
                SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            }
            false ->{
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            }
        }
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

}