package com.davinciapp.holmesclub.model

import android.graphics.Typeface

data class WritingStyle(
    val size: Float = 18F,
    val padding: Int = 8,
    val typeFace: Int = Typeface.NORMAL){

    enum class Styles {
        MEDIUM, SMALL, BIG
    }
}

