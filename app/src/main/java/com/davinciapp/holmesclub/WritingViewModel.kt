package com.davinciapp.holmesclub

import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davinciapp.holmesclub.model.Bloc
import com.davinciapp.holmesclub.model.ImageBloc
import com.davinciapp.holmesclub.model.TextBloc

class WritingViewModel(private val jsonParser: JsonParser) : ViewModel() {

    private val blocsMutable = MutableLiveData<List<Bloc>>()
    val blocs: LiveData<List<Bloc>> = blocsMutable

    val testJson = MutableLiveData<String>()

    init {
        //Get draft if existing
        readAndExposeJson("""[{"style":"BIG","text":"Titre","blocType":"Text"},{"style":"MEDIUM","text":"Corps de l\u0027article","blocType":"Text"}]""")

    }

    fun saveDraft(layout: LinearLayout) {
        val jsonArticle = convertToJson(layout)
        Log.d("debuglog", jsonArticle)
        readAndExposeJson(jsonArticle)
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
            } else if (bloc is ImageView) {
                blocs.add(ImageBloc())
            }
        }

        return  jsonParser.parseToJson(blocs)
    }

    fun readAndExposeJson(jsonStr: String){
        val blocs = jsonParser.deserializeBlocs(jsonStr)
        blocsMutable.value = blocs
    }

}