package com.davinciapp.holmesclub

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.core.content.res.ResourcesCompat
import com.davinciapp.holmesclub.WritingStyle.Styles


class TextBlocWidget(context: Context)
    : androidx.appcompat.widget.AppCompatEditText(context) {

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

    }

    //--------------------------------------------------------------------------------------------//
    //                                         S T Y L E S
    //--------------------------------------------------------------------------------------------//
    fun setStyle(styleType: Styles) {
        val writingStyle =
            when (styleType) {
            Styles.SMALL -> {
                textStyle = Styles.SMALL
                WritingStyle(16F)
            }
            Styles.BIG -> {
                textStyle = Styles.BIG
                WritingStyle(24F, 16, Typeface.BOLD)
            }
            else -> {
                Styles.MEDIUM
                WritingStyle()
            }
        }

        this.textSize = writingStyle.size
        this.setTypeface(ResourcesCompat.getFont(context, R.font.economica), writingStyle.typeFace)
        this.setPadding(8, writingStyle.padding, 8, writingStyle.padding)
    }


    //Always starts with an invisible space -> help us know when to delete the edit text
    private fun placeBeginIndicator() {
        val string = SpannableString(" ")
        string.setSpan(
            ImageSpan(context, android.R.drawable.divider_horizontal_dark),
            0,
            1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.setText(string)
    }

    interface SpecialKeyPressed {
        fun onEnterPressed(endText: CharSequence)
        fun onReturnPressed(text: CharSequence)
    }

}