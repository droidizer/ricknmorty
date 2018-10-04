package com.guru.ricknmorty.utils

import android.databinding.BindingAdapter
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView


@BindingAdapter("loadImage", "rounded")
fun SimpleDraweeView.loadImage(url: String?, rounded: Boolean = false) {
    if (url == null) {
        return
    }
    if (rounded) {
        hierarchy.roundingParams = RoundingParams.asCircle()
    }
    setImageURI(url)
}