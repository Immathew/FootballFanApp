package com.example.footballfanapp.bindingAdapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

class UpcomingMatchesRowBinding {
    companion object {
        @BindingAdapter("updateMatchTime", requireAll = true)
        @JvmStatic
        fun updateMatchTime(textView: TextView, time: String) {
            textView.text = time.substring(11, 16)
        }

    }

}