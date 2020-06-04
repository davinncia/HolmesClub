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

class EditorViewModel(private val jsonParser: JsonParser, private val draftRepo: DraftRepository) : ViewModel() {

    private val blocsMutable = MutableLiveData<List<Bloc>>()
    val blocs: LiveData<List<Bloc>> = blocsMutable

    val testJson = MutableLiveData<String>()

    var jsonArticle: String = ""

    init {
        //Get draft if existing
        if (jsonArticle.isEmpty()) {
            readAndExposeJson("""[{"style":"BIG","text":"Titre","blocType":"Text"},{"style":"MEDIUM","text":"Corps de l\u0027article","blocType":"Text"},{"resId":2131099750,"blocType":"Image"},{"style":"MEDIUM","text":"Héhé ","blocType":"Text"}]""")
        } else {
            readAndExposeJson(jsonArticle)
        }

    }

    fun saveDraft(layout: LinearLayout) {
        jsonArticle = convertToJson(layout)
        Log.d("debuglog", "SAVED")
        readAndExposeJson(jsonArticle)

        viewModelScope.launch {
            val draft = Draft("title", jsonArticle, System.currentTimeMillis())
            draftRepo.insert(draft)
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

        return  jsonParser.parseToJson(blocs)
    }

    fun readAndExposeJson(jsonStr: String){
        val blocs = jsonParser.deserializeBlocs(jsonStr)
        blocsMutable.value = blocs
    }

}