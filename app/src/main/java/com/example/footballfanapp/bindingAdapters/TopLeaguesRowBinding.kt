package com.example.footballfanapp.bindingAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load



class TopLeaguesRowBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
             imageView.load(imageUrl) {

                crossfade(600)
                }

        }
    }
}