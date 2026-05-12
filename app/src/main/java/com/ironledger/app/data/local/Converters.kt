package com.ironledger.app.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ironledger.app.data.local.entity.ExerciseLog
import com.ironledger.app.data.local.entity.TemplateExercise

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromExerciseLogList(value: List<ExerciseLog>?): String? {
        if (value == null) return null
        return gson.toJson(value)
    }

    @TypeConverter
    fun toExerciseLogList(value: String?): List<ExerciseLog>? {
        if (value == null) return null
        val type = object : TypeToken<List<ExerciseLog>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromTemplateExerciseList(value: List<TemplateExercise>?): String? {
        if (value == null) return null
        return gson.toJson(value)
    }

    @TypeConverter
    fun toTemplateExerciseList(value: String?): List<TemplateExercise>? {
        if (value == null) return null
        val type = object : TypeToken<List<TemplateExercise>>() {}.type
        return gson.fromJson(value, type)
    }
}
