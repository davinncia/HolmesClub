package com.davinciapp.holmesclub.view.editor

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.bind
import com.davinciapp.holmesclub.di.ViewModelFactory
import com.davinciapp.holmesclub.model.*
import com.davinciapp.holmesclub.view.editor.widgets.MyImageBlocWidget
import com.davinciapp.holmesclub.view.editor.widgets.SeparatorBlocWidget
import com.davinciapp.holmesclub.view.editor.widgets.TextBlocWidget


class EditorActivity : AppCompatActivity(), MyImageBlocWidget.OnClearImageBlocClickListener {

    private val titleView by bind<EditText>(R.id.et_draft_title_editor)

    private lateinit var viewModel: EditorViewModel
    private val layout by bind<LinearLayout>(R.id.linear_layout_blocs_editor)

    //DEBUG
    private var isJson = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val draftId = intent.getIntExtra(DRAFT_ID_KEY, -1)

        //VIEWS
        val coverImageView = findViewById<ImageView>(R.id.iv_cover_editor)

        //CLICKS
        findViewById<ImageView>(R.id.iv_font_size_editor).setOnClickListener {
            showFontOptionsPopUp(
                it
            )
        }
        findViewById<ImageView>(R.id.iv_paragraph_editor).setOnClickListener {
            showParagraphOptionsPopUp(
                it
            )
        }
        findViewById<ImageView>(R.id.iv_quote_editor).setOnClickListener { setQuoteStyle() }
        findViewById<ImageView>(R.id.iv_separator_editor).setOnClickListener { addSeparator() }
        findViewById<ImageView>(R.id.iv_image_editor).setOnClickListener {
            //selectPicture()
            //COUNT DEBUG
            val bloc = currentFocus
            if (bloc is TextBlocWidget) {
                val count = viewModel.countWordsNbr(bloc.text.toString())
                Toast.makeText(this, "$count words", Toast.LENGTH_SHORT).show()
            }
        }
        //Cover pic
        findViewById<ImageView>(R.id.iv_choose_cover_picture_editor).setOnClickListener { selectCoverPicture() }

        //Getting ViewModel via Factory
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(application)
        )[EditorViewModel::class.java]

        //Sending draft id to vm
        viewModel.getDraft(draftId)

        //Listening
        viewModel.draftUi.observe(this, Observer {
            Glide.with(this).load(Uri.parse(it.coverPictureUri)).centerCrop().into(coverImageView)
            //Glide.with(this).load("android.resource://com.davinciapp.holmesclub/drawable/ic_sherlock").into(coverImageView)
            //Glide.with(this).load(R.drawable.ic_sherlock).into(coverImageView)
            titleView.setText(it.title)
            displayBlocsOnView(it.blocs)
        })
        /*
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
        */

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
        val popWindow = PopupWindow(
            popView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popView.findViewById<ImageView>(R.id.iv_font_big_writing).setOnClickListener {
            //BIG
            currentFocus?.let {
                if (it is TextBlocWidget) {
                    it.setStyle(WritingStyle.Styles.MEDIUM)
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
        val popWindow = PopupWindow(
            popView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

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
    private fun selectPicture() {
        if (!hasReadStoragePermission()) askReadStoragePermission()
        if (hasReadStoragePermission()) launchGalleryIntent(BLOC_PIC_RQ)
    }

    private fun addPictureBloc(uri: String) {

        addViewBelowFocus(
            MyImageBlocWidget(
                this,
                uri,
                this
            )
        )
    }

    //--------------------------------------------------------------------------------------------//
    //                                     U I     A C T I O N
    //--------------------------------------------------------------------------------------------//

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
                            bloc.uri,
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

    //--------------------------------------------------------------------------------------------//
    //                                      I M A G E S
    //--------------------------------------------------------------------------------------------//
    private fun selectCoverPicture() {
        if (!hasReadStoragePermission()) askReadStoragePermission()
        if (hasReadStoragePermission()) launchGalleryIntent(COVER_PIC_RQ)
    }

    private fun launchGalleryIntent(rq: Int) {
        //val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        //getIntent.type = "image/*"

        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"
        startActivityForResult(pickIntent, rq)

        //val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        //startActivityForResult(chooserIntent, GALLERY_RQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && resultCode == Activity.RESULT_OK && requestCode == COVER_PIC_RQ) {
            //COVER
            viewModel.setPictureAsCover(data)
        } else if (data != null && resultCode == Activity.RESULT_OK && requestCode == BLOC_PIC_RQ) {
            data.dataString?.let {
                //viewModel.figurePictureOrientation(it) //Doesn't yet work
                //Pic isn't save for now has it already comes from the MediaStore
                addPictureBloc(it)
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

    //DEBUG
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_json_writing -> {


                /*
                isJson = if (isJson) {
                    val json = (layout.getChildAt(0) as TextView).text.toString()
                    //viewModel
                    false
                } else {
                    //viewModel.showJson(layout)
                    true
                }
                Toast.makeText(this, "JSON", Toast.LENGTH_SHORT).show()

                 */
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //--------------------------------------------------------------------------------------------//
    //                                   P E R M I S S I O N
    //--------------------------------------------------------------------------------------------//
    private fun hasReadStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askReadStoragePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_PERMISSION_RQ
        )
    }

    /*
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            READ_PERMISSION_RQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //Not granted
                } else {
                    //Granted
                }
            }
        }
    }

     */


    //--------------------------------------------------------------------------------------------//

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Toast.makeText(applicationContext, "DOWN", Toast.LENGTH_SHORT).show()
        return super.onKeyDown(keyCode, event)
    }


    override fun onStop() {
        viewModel.saveDraft(layout, titleView.text.toString())
        super.onStop()
    }

    companion object {
        const val DRAFT_ID_KEY = "draft_id_key"
        const val COVER_PIC_RQ = 11
        const val BLOC_PIC_RQ = 12
        const val READ_PERMISSION_RQ = 20

        fun newIntent(context: Context, draftId: Int = -1): Intent {
            val intent = Intent(context, EditorActivity::class.java)
            intent.putExtra(DRAFT_ID_KEY, draftId)
            return intent
        }
    }

    override fun onClearImageBlocClicked(imageBlocWidget: MyImageBlocWidget) {
        layout.removeView(imageBlocWidget)
    }
}
