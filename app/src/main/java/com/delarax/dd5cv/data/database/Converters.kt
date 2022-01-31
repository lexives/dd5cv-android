package com.delarax.dd5cv.data.database

import androidx.room.TypeConverter
import com.delarax.dd5cv.models.characters.Ability
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import com.delarax.dd5cv.models.characters.DeathSave
import com.delarax.dd5cv.models.characters.Proficiency
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Singleton

@Singleton
class Converters {
    // enableComplexMapKeySerialization is needed to convert from a String to a Map
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    @TypeConverter
    fun fromStringList(list: List<String>?) : String {
        return list?.let {
            val type: Type = object : TypeToken<List<String>>() {}.type
            gson.toJson(list, type)
        } ?: ""
    }

    @TypeConverter
    fun toStringList(string: String?) : List<String> {
        return string?.let {
            val type: Type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(string, type)
        } ?: listOf()
    }

    @TypeConverter
    fun fromClassLevelList(list: List<CharacterClassLevel>?) : String {
        return list?.let {
            val type: Type = object : TypeToken<List<CharacterClassLevel>>() {}.type
            gson.toJson(list, type)
        } ?: ""
    }

    @TypeConverter
    fun toClassLevelList(string: String?) : List<CharacterClassLevel> {
        return string?.let {
            val type: Type = object : TypeToken<List<CharacterClassLevel>>() {}.type
            gson.fromJson(string, type)
        } ?: listOf()
    }

    @TypeConverter
    fun fromAbilityScoreMap(map: Map<Ability, Int?>?) : String {
        return map?.let {
            val type: Type = object : TypeToken<Map<Ability, Int?>>() {}.type
            gson.toJson(map, type)
        } ?: ""
    }

    @TypeConverter
    fun toAbilityScoreMap(string: String?) : Map<Ability, Int?> {
        return string?.let {
            val type: Type = object : TypeToken<Map<Ability, Int?>>() {}.type
            // {"Ability(name\u003dability 1, abbreviation\u003dabbrev 1)":10,"Ability(name\u003dability 2, abbreviation\u003dabbrev 2)":15}
            gson.fromJson(string, type)
        } ?: mapOf()
    }

    @TypeConverter
    fun fromProficiencyList(list: List<Proficiency>?) : String {
        return list?.let {
            val type: Type = object : TypeToken<List<Proficiency>>() {}.type
            gson.toJson(list, type)
        } ?: ""
    }

    @TypeConverter
    fun toProficiencyList(string: String?) : List<Proficiency> {
        return string?.let {
            val type: Type = object : TypeToken<List<Proficiency>>() {}.type
            gson.fromJson(string, type)
        } ?: listOf()
    }

    @TypeConverter
    fun fromDeathSave(list: DeathSave?) : String {
        return list?.let {
            val type: Type = object : TypeToken<DeathSave>() {}.type
            gson.toJson(list, type)
        } ?: ""
    }

    @TypeConverter
    fun toDeathSave(string: String?) : DeathSave {
        return string?.let {
            val type: Type = object : TypeToken<DeathSave>() {}.type
            gson.fromJson(string, type)
        } ?: DeathSave()
    }
}