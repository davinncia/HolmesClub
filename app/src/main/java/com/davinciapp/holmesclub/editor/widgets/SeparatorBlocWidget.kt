package com.davinciapp.holmesclub.editor.widgets

import android.content.Context
import com.davinciapp.holmesclub.R

class SeparatorBlocWidget(context: Context) : androidx.appcompat.widget.AppCompatImageView(context) {

    val resId = R.drawable.divider_one

    init {
        this.setImageResource(resId)

        //Layout Params
        //this.setPadding(System.scree / 2, 8, this.width / 2, 8)

        //Focus behaviour
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                this.background = context.getDrawable(R.drawable.selection_bloc_frame)
            } else {
                this.background = null
            }
        }
        this.setOnClickListener { it.requestFocus() }

    }

}