package com.davinciapp.holmesclub.model

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

data class SeparatorBloc(
    val resId: Int
): Bloc() {
    init {
        blocType = "Separator"
    }
}

data class ImageBloc(
    val uri: String
) : Bloc() {
    init {
        blocType = "Image"
    }
}