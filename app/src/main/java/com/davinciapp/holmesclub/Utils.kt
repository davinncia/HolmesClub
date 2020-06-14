package com.davinciapp.holmesclub

import android.app.Activity
import android.content.res.Resources
import android.view.View
import androidx.annotation.IdRes

fun <T: View> Activity.bind(@IdRes id: Int): Lazy<T> {
    return lazy { findViewById<T>(id) }
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()