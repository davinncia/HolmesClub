package com.davinciapp.holmesclub.json

import com.davinciapp.holmesclub.model.Bloc
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type


class BlocDeserializer(private val blocTypeElementName: String) : JsonDeserializer<Bloc> {

    private val gson = Gson()
    private val blocTypeRegistry = hashMapOf<String, Class<out Bloc?>>()

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Bloc? {

        val blocObject = json.asJsonObject
        val blocTypeElement = blocObject[blocTypeElementName]

        val blocType: Class<out Bloc?>? = blocTypeRegistry[blocTypeElement.asString]

        return gson.fromJson(blocObject, blocType)
    }

    fun registerBlocType(blocTypeName: String, blocType: Class<out Bloc?>) {
        blocTypeRegistry[blocTypeName] = blocType
    }

}