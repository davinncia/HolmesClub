package com.davinciapp.holmesclub.view.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.model.*

class ArticleViewModel : ViewModel() {

    private val blocsMutable = MutableLiveData<List<Bloc>>()
    val blocs: LiveData<List<Bloc>> = blocsMutable

    init {
        //DEBUG
        blocsMutable.value = listOf(
            TextBloc("Test bloc text", WritingStyle.Styles.QUOTE),
            SeparatorBloc(R.drawable.divider_one),
            ImageBloc("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRo0vzt37AqGrtN4pKM8r06vcZLr-UFWlLwh5YuykhA99EuXV0o&usqp=CAU")
        )
    }
}