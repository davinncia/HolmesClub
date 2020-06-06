package com.davinciapp.holmesclub.editor

import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinciapp.holmesclub.drafts.model.Draft
import com.davinciapp.holmesclub.editor.widgets.MyImageBlocWidget
import com.davinciapp.holmesclub.editor.widgets.SeparatorBlocWidget
import com.davinciapp.holmesclub.editor.widgets.TextBlocWidget
import com.davinciapp.holmesclub.json.JsonParser
import com.davinciapp.holmesclub.model.Bloc
import com.davinciapp.holmesclub.model.ImageBloc
import com.davinciapp.holmesclub.model.SeparatorBloc
import com.davinciapp.holmesclub.model.TextBloc
import com.davinciapp.holmesclub.repository.DraftRepository
import kotlinx.coroutines.launch

class EditorViewModel(private val jsonParser: JsonParser, private val draftRepo: DraftRepository) :
    ViewModel() {

    private val blocsMutable = MutableLiveData<List<Bloc>>()
    val blocs: LiveData<List<Bloc>> = blocsMutable

    val testJson = MutableLiveData<String>()

    //new draft = -1
    private var draftId: Int? = null

    fun getDraft(id: Int) {
        //Getting draft if not already done
        if (draftId == null) {
            draftId = id //Memorize draft id
            if (draftId != -1) {
                Log.d("debuglog", "GETTING DRAFT FROM DB: $id")
                //Already existing draft
                viewModelScope.launch {
                    val draft = draftRepo.getDraft(id)
                    readAndExposeJson(draft.content)
                }
            } else {
                Log.d("debuglog", "New draft !")
                //New draft
                readAndExposeJson("""[{"style":"BIG","text":"Titre","blocType":"Text"},{"style":"MEDIUM","text":"Corps de l\u0027article","blocType":"Text"},{"resId":2131099750,"blocType":"Image"},{"style":"MEDIUM","text":"Héhé ","blocType":"Text"}]""")
            }
        } else {
            Log.d("debuglog", "We already got it")
        }

    }

    fun saveDraft(layout: LinearLayout) {
        val jsonArticle = convertToJson(layout)
        readAndExposeJson(jsonArticle) //DEBUG

        viewModelScope.launch {
            //Draft object
            var title = "unknown"
            val titleBloc = layout.getChildAt(0)
            if (titleBloc is TextBlocWidget) {
                title = titleBloc.text.toString()
            }
            val draft = Draft(title, jsonArticle, System.currentTimeMillis())

            //Db instructions
            if (draftId == -1) {
                draftRepo.insert(draft)
                Log.d("debuglog", "NEW DRAFT SAVED")
            } else {
                draftId?.let {
                    draft.id = it
                    draftRepo.update(draft)
                    Log.d("debuglog", "DRAFT UPDATED")
                }
            }
        }

    }

    //DEBUG
    fun showJson(layout: LinearLayout) {
        val jsonArticle = convertToJson(layout)
        //DEBUG
        testJson.value = jsonArticle
    }

    private fun convertToJson(layout: LinearLayout): String {
        val blocs = arrayListOf<Bloc>()

        for (i in 0 until layout.childCount) {
            val bloc = layout.getChildAt(i)
            if (bloc is TextBlocWidget) {
                blocs.add(TextBloc(bloc.text.toString(), bloc.textStyle))
            } else if (bloc is MyImageBlocWidget) {
                blocs.add(ImageBloc())
            } else if (bloc is SeparatorBlocWidget) {
                blocs.add(SeparatorBloc(bloc.resId))
            }
        }

        return jsonParser.parseToJson(blocs)
    }

    fun readAndExposeJson(jsonStr: String) {
        val blocs = jsonParser.deserializeBlocs(jsonStr)
        blocsMutable.value = blocs
    }

}