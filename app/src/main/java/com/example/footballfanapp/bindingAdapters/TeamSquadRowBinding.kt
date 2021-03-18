package com.example.footballfanapp.bindingAdapters

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.footballfanapp.util.DateFormatter.Companion.getDateWithServerTimeStamp
import java.text.SimpleDateFormat

class TeamSquadRowBinding {

    companion object {

        @SuppressLint("SimpleDateFormat")
        @BindingAdapter("displayDateOfBirth")
        @JvmStatic
        fun displayDateOfBirth(textView: TextView, date: String) {
            val dateOfBirth = date.getDateWithServerTimeStamp()
            textView.text = SimpleDateFormat("dd-MM-yyyy").format(dateOfBirth!!).toString()
        }
    }
}