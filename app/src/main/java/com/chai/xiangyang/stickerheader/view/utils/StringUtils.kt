package jp.co.solxyz.fleeksorm.utils

import com.google.gson.Gson
import com.google.gson.JsonParser

object StringUtils {
    private val gson = Gson()
    fun <T> toObject(source: String, clazz: Class<T>): T {
        return gson.fromJson(source, clazz)
    }

    fun <T> toList(source: String, clazz: Class<T>): MutableList<T> {
        val list = mutableListOf<T>()
        val jsonArray = JsonParser().parse(source).asJsonArray
        for (item in jsonArray) {
            val t = gson.fromJson(item.toString(), clazz)
            list.add(t)
        }
        return list
    }

}