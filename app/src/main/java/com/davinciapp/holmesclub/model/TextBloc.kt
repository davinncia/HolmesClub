package com.davinciapp.holmesclub.model

import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.WritingStyle

//Parent class precizing type for Json deserialization
abstract class Bloc{
    var blocType = "Animal"
}

data class TextBloc(
    val text: String,
    val style: WritingStyle.Styles
): Bloc() {
    init {
        blocType = "Text"
    }
}

data class ImageBloc(
    val resId: Int = R.drawable.ic_font_size
) : Bloc() {
    init {
        blocType = "Image"
    }
}