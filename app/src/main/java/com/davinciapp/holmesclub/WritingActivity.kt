package com.davinciapp.holmesclub

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import com.davinciapp.holmesclub.model.Bloc
import com.davinciapp.holmesclub.model.ImageBloc
import com.davinciapp.holmesclub.model.TextBloc

class WritingActivity : AppCompatActivity() {

    private val layout by bind<LinearLayout>(R.id.linear_layout_writing)

    //DEBUG
    private var isJson = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        addTextBloc()

        //CLICKS
        findViewById<ImageView>(R.id.iv_font_size_writing).setOnClickListener { showFontOptionsPopUp(it) }
        findViewById<ImageView>(R.id.iv_paragraph_writing).setOnClickListener { showParagraphOptionsPopUp(it) }
    }


    //--------------------------------------------------------------------------------------------//
    //                                      P O P   UP   M E N U
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

    private fun showFontOptionsPopUp(view: View) {
        //POP UP WINDOW
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popView = inflater.inflate(R.layout.popup_window_font, null, false)
        val popup = PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popView.findViewById<ImageView>(R.id.iv_font_big_writing).setOnClickListener {
            //BIG
            currentFocus?.let {
                if (it is TextBlocWidget) {
                    it.styleBig()
                }
            }
        }

        popView.findViewById<ImageView>(R.id.iv_font_small_writing).setOnClickListener {
            //SMALL
            currentFocus?.let {
                if (it is TextBlocWidget) {
                    it.styleSmall()
                }
            }
        }
        popup.showAsDropDown(view, 50, -250)
    }

    private fun showParagraphOptionsPopUp(view: View) {
        //POP UP WINDOW
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popView = inflater.inflate(R.layout.popup_window_paragraph, null, false)
        val popWindow = PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popView.findViewById<ImageView>(R.id.iv_paragraph_add_writing).setOnClickListener {
            //ADD TEXT BLOC and get focus
            layout.addView(TextBlocWidget(this))
            currentFocus?.let {
                val index = layout.indexOfChild(it)
                layout.getChildAt(index + 1).requestFocus()
            }
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


    //--------------------------------------------------------------------------------------------//
    //                                       A C T I O N
    //--------------------------------------------------------------------------------------------//
    private fun addTextBloc() {


        layout.addView(TextBlocWidget(this))/*, object : TextBloc.SpecialKeyPressed {
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

    private fun parseToJson() {

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
    }

    private fun parseToBlocs() {
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
                    parseToBlocs()
                    false
                } else {
                    parseToJson()
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
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, WritingActivity::class.java)
        }
    }
}
