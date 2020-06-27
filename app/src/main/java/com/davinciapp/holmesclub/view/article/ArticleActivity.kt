package com.davinciapp.holmesclub.view.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.bind
import com.davinciapp.holmesclub.di.ViewModelFactory
import com.davinciapp.holmesclub.model.*

class ArticleActivity : AppCompatActivity() {

    private val layout by bind<LinearLayout>(R.id.linear_layout_blocs_article)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(application))[ArticleViewModel::class.java]

        viewModel.blocs.observe(this, Observer {
            displayBlocs(it)
        })
    }

    private fun displayBlocs(blocs: List<Bloc>) {

        for (bloc in blocs) {

            val view: View

            when (bloc) {
                is TextBloc -> {
                    view = createStyledTextView(bloc.style)
                    view.text = bloc.text
                }
                is SeparatorBloc -> {
                    view = ImageView(this)
                    view.setImageResource(bloc.resId)
                }
                is ImageBloc -> {
                    view = ImageView(this)
                    Glide.with(this).load(bloc.uri).into(view)
                    //Glide.with(this).load(R.drawable.ic_launcher_foreground).into(view)
                }
                else -> throw ClassNotFoundException("Bloc type not recognized.")
            }
            layout.addView(view)
        }
    }

    private fun createStyledTextView(styleType: WritingStyle.Styles): TextView {
        val textView = TextView(this)

        val writingStyle = when (styleType) {
            WritingStyle.Styles.QUOTE -> WritingStyle.QUOTE
            WritingStyle.Styles.SMALL -> WritingStyle.SMALL
            else -> WritingStyle.MEDIUM
        }

        textView.textSize = writingStyle.size
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.economica), writingStyle.typeFace)
        textView.setPadding(writingStyle.paddingStart, writingStyle.paddingTop, 8, writingStyle.paddingBottom)
        textView.setTextColor(this.resources.getColor(writingStyle.colorRes))
        textView.setBackgroundResource(writingStyle.backgroundRes)

        return textView
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ArticleActivity::class.java)
    }
}