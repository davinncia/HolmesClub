package com.davinciapp.holmesclub.model

import com.davinciapp.holmesclub.R

abstract class Bloc{
    var type = "Animal"
}

data class TextBloc(
    val text: String
): Bloc() {
    init {
        type = "Text"
    }
}

data class ImageBloc(
    val resId: Int = R.drawable.ic_font_size
) : Bloc() {
    init {
        type = "Image"
    }
}