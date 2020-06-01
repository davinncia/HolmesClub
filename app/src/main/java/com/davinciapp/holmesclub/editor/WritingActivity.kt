package com.davinciapp.holmesclub.editor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.davinciapp.holmesclub.*
import com.davinciapp.holmesclub.di.ViewModelFactory
import com.davinciapp.holmesclub.editor.widgets.MyImageBlocWidget
import com.davinciapp.holmesclub.editor.widgets.SeparatorBlocWidget
import com.davinciapp.holmesclub.editor.widgets.TextBlocWidget
import com.davinciapp.holmesclub.model.*

class WritingActivity : AppCompatActivity(), MyImageBlocWidget.OnClearImageBlocClickListener {

    //TODO: there should always be a least 1 bloc

    private lateinit var viewModel: WritingViewModel
    private val layout by bind<LinearLayout>(R.id.linear_layout_writing)

    //DEBUG
    private var isJson = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        //CLICKS
        findViewById<ImageView>(R.id.iv_font_size_writing).setOnClickListener { showFontOptionsPopUp(it) }
        findViewById<ImageView>(R.id.iv_paragraph_writing).setOnClickListener { showParagraphOptionsPopUp(it) }
        findViewById<ImageView>(R.id.iv_quote_writing).setOnClickListener { setQuoteStyle() }
        findViewById<ImageView>(R.id.iv_separator_writing).setOnClickListener { addSeparator() }
        findViewById<ImageView>(R.id.iv_image_writing).setOnClickListener { addPictureBloc() }

        //Getting ViewModel via Factory
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(application))[WritingViewModel::class.java]

        //Listening
        viewModel.blocs.observe(this, Observer {
            //Retrieved draft
            //DEBUG
            layout.removeAllViews()
            displayBlocsOnView(it)
        })

        //DEBUG
        viewModel.testJson.observe(this, Observer {
            //Display JSON
            val textView = TextView(this)
            textView.text = it
            layout.removeAllViews()
            layout.addView(textView)
        })

        addTextBloc()



    }


    //--------------------------------------------------------------------------------------------//
    //                                      T O O L   B A R
    //--------------------------------------------------------------------------------------------//
    private fun handleToolBarClicks(view: View) {
        /*when (view.id) {
            R.id.iv_paragraph_writing -> {
                val pop = PopupMenu(this, view)
                pop.menuInflater.inflate(R.menu.paragraph_pop_up_menu, pop.menu)

                pop.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.item_pop_up_add_paragraph -> Toast.makeText(this, "ADD", Toast.LENGTH_SHORT).show()
                        R.id.item_pop_up_remove_paragraph -> Toast.makeText(this, "ADD", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                val popHelper = MenuPopupHelper(this, pop.menu as MenuBuilder, view)
                popHelper.setForceShowIcon(true)
                popHelper.show()
            }
        }

         */


    }

    //FONT STYLE
    private fun showFontOptionsPopUp(view: View) {
        //POP UP WINDOW
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popView = inflater.inflate(R.layout.popup_window_font, null, false)
        val popWindow = PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popView.findViewById<ImageView>(R.id.iv_font_big_writing).setOnClickListener {
            //BIG
            currentFocus?.let {
                if (it is TextBlocWidget) {
                    it.setStyle(WritingStyle.Styles.BIG)
                }
            }
            popWindow.dismiss()
        }

        popView.findViewById<ImageView>(R.id.iv_font_small_writing).setOnClickListener {
            //SMALL
            currentFocus?.let {
                if (it is TextBlocWidget) {
                    it.setStyle(WritingStyle.Styles.SMALL)
                }
            }
            popWindow.dismiss()
        }
        popWindow.showAsDropDown(view, 50, -250)
    }

    //PARAGRAPH OPTIONS
    private fun showParagraphOptionsPopUp(view: View) {
        //POP UP WINDOW
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popView = inflater.inflate(R.layout.popup_window_paragraph, null, false)
        val popWindow = PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popView.findViewById<ImageView>(R.id.iv_paragraph_add_writing).setOnClickListener {
            //ADD text bloc at next position
            addViewBelowFocus(
                TextBlocWidget(
                    this
                )
            )

            //currentFocus?.let {
            //    val index = layout.indexOfChild(it)
            //    layout.getChildAt(index + 1).requestFocus()
            //}
            popWindow.dismiss()
        }

        popView.findViewById<ImageView>(R.id.iv_paragraph_delete_writing).setOnClickListener {
            //REMOVE and move text
            var text = ""

            currentFocus?.let {
                if (it is TextBlocWidget) {
                    text = it.text.toString()
                }
                layout.removeView(it)
            }

            val child = layout.getChildAt(layout.childCount - 1)
            child?.requestFocus()

            if (child is TextBlocWidget) {
                //ELSE find one
                child.setText("${child.text}$text")
            }

            popWindow.dismiss()
        }
        popWindow.showAsDropDown(view, 50, -250)
    }

    //QUOTE
    private fun setQuoteStyle() {

        currentFocus?.let {
            if (it is TextBlocWidget) {
                if (it.textStyle == WritingStyle.Styles.QUOTE) {
                    it.setStyle(WritingStyle.Styles.MEDIUM) //Back to default
                } else {
                    it.setStyle(WritingStyle.Styles.QUOTE) //Quote style
                }
            }
        }

    }

    //SEPARATOR
    private fun addSeparator() {
        addViewBelowFocus(SeparatorBlocWidget(this))
    }

    //PICTURE
    private fun addPictureBloc() {
        addViewBelowFocus(
            MyImageBlocWidget(
                this,
                this
            )
        )
    }

    //--------------------------------------------------------------------------------------------//
    //                                          U I
    //--------------------------------------------------------------------------------------------//
    private fun addTextBloc() {
        val textBloc =
            TextBlocWidget(this)
        layout.addView(textBloc)

        /*, object : TextBloc.SpecialKeyPressed {
            override fun onEnterPressed(endText: CharSequence) {
                Toast.makeText(applicationContext, endText, Toast.LENGTH_SHORT).show()
                addTextBloc()
            }

            override fun onReturnPressed(text: CharSequence) {
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }
        }))
        */
    }


    private fun addViewBelowFocus(view: View) {
        currentFocus?.let {
            layout.addView(view, layout.indexOfChild(it) + 1)
        } ?: layout.addView(view)
    }

    private fun displayBlocsOnView(blocs: List<Bloc>) {
        for (bloc in blocs) {
            when (bloc) {
                is TextBloc -> {
                    val editText =
                        TextBlocWidget(this)
                    editText.setText(bloc.text)
                    editText.setStyle(bloc.style)
                    layout.addView(editText)
                }
                is ImageBloc -> {
                    layout.addView(
                        MyImageBlocWidget(
                            this,
                            this
                        )
                    )
                    //Fetch picture in BackGround
                }
                is SeparatorBloc -> {
                    layout.addView(
                        SeparatorBlocWidget(this)
                    )
                }
            }
        }
    }

    private fun parseToJson() {
/*
        val blocs = arrayListOf<Bloc>()

        for (i in 0 until layout.childCount) {
            val bloc = layout.getChildAt(i)
            if (bloc is TextBlocWidget) {
                blocs.add(TextBloc(bloc.text.toString()))
            } else if (bloc is ImageView) {
                blocs.add(ImageBloc())
            }
            
            blocs.add(ImageBloc())
        }

        val textView = TextView(this)
        textView.text = JsonParser().parseToJson(blocs)

        layout.removeAllViews()
        layout.addView(textView)

 */
    }

    private fun parseToBlocs() {
        /*

        val json = (layout.getChildAt(0) as TextView).text.toString()
        layout.removeAllViews()
        //val blocs = JsonParser().parseToBlocs(json)
        val blocs = JsonParser().deserializeBlocs(json)
        for (bloc in blocs) {
            if (bloc is TextBloc) {
                val editText = TextBlocWidget(this)
                editText.setText(bloc.text)
                layout.addView(editText)
            } else if (bloc is ImageBloc) {
                val imageView = ImageView(this)
                imageView.background = getDrawable(bloc.resId)
                layout.addView(imageView)
            }

        }
         */
    }

    //--------------------------------------------------------------------------------------------//
    //                                       M E N U
    //--------------------------------------------------------------------------------------------//
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.writing, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_json_writing -> {
                isJson = if (isJson) {
                    val json = (layout.getChildAt(0) as TextView).text.toString()
                    viewModel.readAndExposeJson(json)
                    false
                } else {
                    viewModel.showJson(layout)
                    true
                }
                Toast.makeText(this, "JSON", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Toast.makeText(applicationContext, "DOWN", Toast.LENGTH_SHORT).show()
        return super.onKeyDown(keyCode, event)
    }


    //--------------------------------------------------------------------------------------------//
    override fun onStop() {
        viewModel.saveDraft(layout)
        super.onStop()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, WritingActivity::class.java)
        }
    }

    override fun onClearImageBlocClicked(imageBlocWidget: MyImageBlocWidget) {
        layout.removeView(imageBlocWidget)
    }
}
