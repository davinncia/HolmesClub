package com.davinciapp.holmesclub

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

fun <T: View> Activity.bind(@IdRes id: Int): Lazy<T> {
    return lazy { findViewById<T>(id) }
}