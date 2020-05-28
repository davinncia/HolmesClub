package com.davinciapp.holmesclub.editor

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.davinciapp.holmesclub.R

class MyImageBlocWidget(context: Context, listener: OnClearImageBlocClickListener) : RelativeLayout(context) {

    init {
        val imageView = ImageView(context)
        val clearView = ImageView(context)

        //Set Layout Params
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            //LinearLayout.LayoutParams.WRAP_CONTENT
            200
        )
        this.layoutParams = layoutParams

        //Focus behaviour
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                clearView.visibility = View.VISIBLE
                this.background = context.getDrawable(R.drawable.image_bloc_frame)
            } else {
                this.background = null
                clearView.visibility = View.GONE
            }
        }
        this.setOnClickListener { it.requestFocus() }
        
        //Add an ImageView with placeholder
        val imageParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        imageParams.addRule(CENTER_IN_PARENT)
        imageView.layoutParams = imageParams
        imageView.setImageResource(R.drawable.ic_picture)
        this.addView(imageView)

        //Add remove button
        clearView.setImageResource(R.drawable.ic_clear)
        clearView.setOnClickListener {
            listener.onClearImageBlocClicked(this)
        }
        clearView.setPadding(8, 8, 8, 8)
        val clearParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        clearView.layoutParams = clearParams
        clearParams.addRule(ALIGN_PARENT_RIGHT)
        this.addView(clearView)

        //Ask for focus on creation
        this.requestFocus()

    }

    interface OnClearImageBlocClickListener {
        fun onClearImageBlocClicked(imageBlocWidget: MyImageBlocWidget)
    }
}