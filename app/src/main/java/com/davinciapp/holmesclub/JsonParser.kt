package com.davinciapp.holmesclub

import com.davinciapp.holmesclub.model.Bloc
import com.davinciapp.holmesclub.model.ImageBloc
import com.davinciapp.holmesclub.model.TextBloc
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class JsonParser {

    fun parseToJson(blocs: List<Bloc>): String = Gson().toJson(blocs)

    fun deserializeBlocs(jsonStr: String): List<Bloc> {

        //val blocType = object : TypeToken<List<TextBloc>>(){}.type
        //val blocs = Gson().fromJson<List<TextBloc>>(json, blocType)
        //return blocs

        val deserializer = BlocDeserializer("blocType")
        deserializer.registerBlocType("Text", TextBloc::class.java)
        deserializer.registerBlocType("Image", ImageBloc::class.java)
        val gson = GsonBuilder().registerTypeAdapter(Bloc::class.java, deserializer).create()

        return gson.fromJson(jsonStr, object : TypeToken<List<Bloc>>(){}.type)
    }

}