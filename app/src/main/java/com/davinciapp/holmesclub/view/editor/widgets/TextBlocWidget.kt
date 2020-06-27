package com.davinciapp.holmesclub.view.editor.widgets

import android.content.Context
import android.graphics.PorterDuff
import androidx.core.content.res.ResourcesCompat
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.model.WritingStyle
import com.davinciapp.holmesclub.model.WritingStyle.Styles


class TextBlocWidget(context: Context) : androidx.appcompat.widget.AppCompatEditText(context) {

    var textStyle = Styles.MEDIUM

    init {
        this.backgroundTintMode = PorterDuff.Mode.CLEAR
        this.setStyle(Styles.MEDIUM) //Set default style

        /*
        placeBeginIndicator()

        //Text watcher for special key inputs
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Toast.makeText(context, "toast", Toast.LENGTH_SHORT).show()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null || s.isEmpty() || start >= s.length || start < 0) return

                // If Enter
                if (s.subSequence(start, start + 1).toString().equals("\n", true)) {
                    val textToMove = s.subSequence(start, s.length)
                    this@TextBloc.setText(s.removeSuffix(textToMove))
                    enterListener.onEnterPressed(textToMove)
                }

                //If Return when position 0
                if (false) {
                    enterListener.onReturnPressed(s)
                }

            }
        })

         */

        //Ask for focus on creation
        this.requestFocus()

    }

    //--------------------------------------------------------------------------------------------//
    //                                         S T Y L E S
    //--------------------------------------------------------------------------------------------//
    fun setStyle(styleType: Styles) {
        val writingStyle =
            when (styleType) {
                Styles.SMALL -> {
                    textStyle = Styles.SMALL
                    WritingStyle.SMALL
                }
                Styles.MEDIUM -> {
                    textStyle = Styles.MEDIUM
                    WritingStyle.MEDIUM
                }
                Styles.QUOTE -> {
                    textStyle = Styles.QUOTE
                    WritingStyle.QUOTE
                }

            }

        this.textSize = writingStyle.size
        this.setTypeface(ResourcesCompat.getFont(context, R.font.economica), writingStyle.typeFace)
        this.setPadding(writingStyle.paddingStart, writingStyle.paddingTop, 8, writingStyle.paddingBottom)
        this.setTextColor(context.resources.getColor(writingStyle.colorRes))
        this.setBackgroundResource(writingStyle.backgroundRes)
    }


    interface SpecialKeyPressed {
        fun onEnterPressed(endText: CharSequence)
        fun onReturnPressed(text: CharSequence)
    }

}