package com.davinciapp.holmesclub.model


import android.graphics.Typeface
import com.davinciapp.holmesclub.R

data class WritingStyle(
    val size: Float = 18F,
    val paddingStart: Int = 8,
    val paddingTop: Int = 8,
    val paddingBottom: Int = 8,
    val typeFace: Int = Typeface.NORMAL,
    val colorRes: Int = R.color.black,
    val backgroundRes: Int = 0){

    enum class Styles {
        MEDIUM, SMALL, QUOTE
    }

    companion object {
        //DISPLAY INFO
        val MEDIUM = WritingStyle()
        val QUOTE = WritingStyle(
            18F,
            paddingStart = 46,
            typeFace = Typeface.ITALIC,
            colorRes = R.color.greyDark,
            backgroundRes = R.drawable.background_quote
        )
        val SMALL = WritingStyle(
            15F,
            typeFace = Typeface.BOLD
        )
    }
}



