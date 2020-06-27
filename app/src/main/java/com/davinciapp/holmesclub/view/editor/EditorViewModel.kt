package com.davinciapp.holmesclub.view.editor

import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinciapp.holmesclub.json.JsonParser
import com.davinciapp.holmesclub.model.Bloc
import com.davinciapp.holmesclub.model.ImageBloc
import com.davinciapp.holmesclub.model.SeparatorBloc
import com.davinciapp.holmesclub.model.TextBloc
import com.davinciapp.holmesclub.repository.DraftRepository
import com.davinciapp.holmesclub.view.drafts.model.Draft
import com.davinciapp.holmesclub.view.editor.widgets.MyImageBlocWidget
import com.davinciapp.holmesclub.view.editor.widgets.SeparatorBlocWidget
import com.davinciapp.holmesclub.view.editor.widgets.TextBlocWidget
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditorViewModel(
    private val application: Application,
    private val jsonParser: JsonParser,
    private val draftRepo: DraftRepository)
    : ViewModel() {

    private val blocsMutable = MutableLiveData<List<Bloc>>()
    val blocs: LiveData<List<Bloc>> = blocsMutable

    private val draftUiMutable = MutableLiveData<DraftEditorUi>()
    val draftUi: LiveData<DraftEditorUi> = draftUiMutable

    val testJson = MutableLiveData<String>()

    //new draft = -1
    private var draftId: Int? = null
    private var wordsCount = 0

    fun getDraft(id: Int) {
        //Getting draft if not already done
        if (draftId == null) {
            draftId = id //Memorize draft id
            if (draftId != -1) {
                Log.d("debuglog", "GETTING DRAFT FROM DB: $id")
                //Already existing draft
                viewModelScope.launch {
                    val draft = draftRepo.getDraft(id)
                    mapDraftForUi(draft)
                    //readAndExposeJson(draft.content)
                }
            } else {
                Log.d("debuglog", "New draft !")
                //New draft -> insert
                val emptyBlocJson = """[{"style":"MEDIUM","text":"Let's write !","blocType":"Text"}]"""
                val newDraft = Draft("New draft", emptyBlocJson, 2, System.currentTimeMillis(), "android.resource://com.davinciapp.holmesclub/drawable/ic_sherlock")
                viewModelScope.launch {
                    draftId = draftRepo.insert(newDraft).toInt()
                }
                mapDraftForUi(newDraft)
                //readAndExposeJson("""[{"style":"BIG","text":"Titre","blocType":"Text"},{"style":"MEDIUM","text":"Corps de l\u0027article","blocType":"Text"},{"resId":2131099750,"blocType":"Image"},{"style":"MEDIUM","text":"Héhé ","blocType":"Text"}]""")
            }
        } else {
            Log.d("debuglog", "We already got it")
        }

    }

    private fun mapDraftForUi(draft: Draft) {
        val blocs = jsonParser.deserializeBlocs(draft.content)
        //Expose to view
        draftUiMutable.value = DraftEditorUi(blocs, draft.pictureUri, draft.title)
    }

    private fun convertToJson(layout: LinearLayout): String {
        val blocs = arrayListOf<Bloc>()
        wordsCount = 0

        for (i in 0 until layout.childCount) {
            val bloc = layout.getChildAt(i)
            if (bloc is TextBlocWidget) {
                blocs.add(TextBloc(bloc.text.toString(), bloc.textStyle))
                wordsCount += countWordsNbr(bloc.text.toString())
            } else if (bloc is MyImageBlocWidget) {
                blocs.add(ImageBloc(bloc.uri))
            } else if (bloc is SeparatorBlocWidget) {
                blocs.add(SeparatorBloc(bloc.resId))
            }
        }

        return jsonParser.parseToJson(blocs)
    }

    //--------------------------------------------------------------------------------------------//
    //                                        D A T A B A S E
    //--------------------------------------------------------------------------------------------//
    fun saveDraft(layout: LinearLayout, title: String) {
        val jsonArticle = convertToJson(layout)
        //readAndExposeJson(jsonArticle) //DEBUG

        viewModelScope.launch {
            //Draft object
            //var subTitle = "unknown"
            //val titleBloc = layout.getChildAt(0)
            //if (titleBloc is TextBlocWidget) {
            //    subTitle = titleBloc.text.toString()
            //}

            //Db instructions
            if (draftId == -1) {
                //val draft = Draft(title, jsonArticle, System.currentTimeMillis(), "android.resource://com.davinciapp.holmesclub/drawable/ic_sherlock")
                //draftId = draftRepo.insert(draft).toInt()
                //Log.d("debuglog", "NEW DRAFT SAVED")
            } else {

                draftId?.let {
                    //val draft = Draft(title, jsonArticle, System.currentTimeMillis(), )
                    //draft.id = it
                    //draftRepo.update(draft)
                    draftRepo.updateDraftContent(it, title, jsonArticle, wordsCount, System.currentTimeMillis())
                    Log.d("debuglog", "DRAFT UPDATED")
                }?: throw IllegalStateException("No draft instance.")


            }
        }
    }


    //--------------------------------------------------------------------------------------------//
    //                                      I M A G E S
    //--------------------------------------------------------------------------------------------//
    fun setPictureAsCover(data: Intent) {
        //SAVING IN PHONE
        val uri: String? =
            if (data.data != null) {
                //GALLERY
                data.data.toString()
                //val image = retrieveImageFromMediaStore(data.data)
                //saveImageInMediaStore(image).toString()
            } else {
                //CAMERA
                //Not using camera yet though
                val imageBitmap = data.extras?.get("data") as Bitmap
                saveImageInMediaStore(imageBitmap)?.toString()
            }
        uri?: throw NoSuchFieldException("Image uri could not be found")

        //SAVING URI IN DB
        viewModelScope.launch {
            if (draftId == -1 || draftId == null) {
                throw IllegalStateException("Draft not recognized")
            } else {
                draftRepo.updateCoverPictureUri(draftId!!, uri)
                draftUi.value?.let {
                    //Alert view
                    draftUiMutable.value = it.copy(coverPictureUri = uri)
                }
            }
        }

    }

    /*
    //Could be used if we want to copy image. Doesn't seem necessary
    private fun retrieveBitmapFromMediaStore(uri: Uri): Bitmap {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if (selectedImage != null) {
            Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                cursor.close();
    }
     */

    private fun saveImageInMediaStore(pic: Bitmap): Uri? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, timeStamp)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }

        val resolver = application.contentResolver

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            resolver.openOutputStream(uri)?.use { outputStream ->
                pic.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            }

            resolver.update(uri, values, null, null)

        } ?: throw RuntimeException("MediaStore failed for some reason")
        return uri
    }


    fun figurePictureOrientation(uri: String) {
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(uri) //FILE NOT FOUND EXCEPTION
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation = exif?.getAttribute(ExifInterface.TAG_ORIENTATION)
        orientation?.let {
            Toast.makeText(application, it, Toast.LENGTH_SHORT).show()
        }
    }


    //--------------------------------------------------------------------------------------------//
    //                                      D E B U G
    //--------------------------------------------------------------------------------------------//
    //DEBUG
    fun showJson(layout: LinearLayout) {
        val jsonArticle = convertToJson(layout)
        //DEBUG
        testJson.value = jsonArticle
    }

    fun countWordsNbr(str: String): Int {
        val words = str.trim().split(("\\s|\\n").toRegex()).toTypedArray().filter { it.isNotEmpty() }
        return words.size
    }

    //--------------------------------------------------------------------------------------------//
    //                                      U I    M O D E L
    //--------------------------------------------------------------------------------------------//
    data class DraftEditorUi(
        val blocs: List<Bloc>,
        val coverPictureUri: String,
        val title: String = "UNKNOWN TITLE"
    )
}