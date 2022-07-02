package com.example.android.politicalpreparedness.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("viewVisibility")
fun bindViewVisibility(view: View, shouldShow: Boolean) {
    view.let {
        if (shouldShow) {
            it.visibility = View.VISIBLE
        } else {
            it.visibility = View.GONE
        }
    }
}