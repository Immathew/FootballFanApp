package com.example.footballfanapp.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    companion object {

        fun String.getDateWithServerTimeStamp(): Date? {
            val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault()
            )
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
            return try {
                dateFormat.parse(this)
            } catch (e: ParseException) {
                null
            }
        }
    }

}