package com.example.myapplication.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import java.util.*
import javax.inject.Inject

class LocalHelper @Inject constructor(base: Context) {

    private val preference: SharedPreferences

    fun setLocale(c: Context, language: String): Context {
        KEY_LANGUAGE = language
        return updateResources(c, language)
    }

    fun setLocale(c: Context): Context {
        return updateResources(c, KEY_LANGUAGE)
    }

    companion object{
        var _KEY_LANGUAGE = "KEY_LANGUAGE"
        var _KEY_USER_ID = "KEY_USER"
        var _KEY_USER_NAME = "KEY_USER_NAME"
        var _KEY_USER_TYPE = "KEY_USER_TYPE"
    }

    init {
        preference = base.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    }

    var KEY_LANGUAGE: String
        set(value) = preference.edit().putString(_KEY_LANGUAGE, value).apply()
        get() = preference.getString(_KEY_LANGUAGE, "My_Lang")!!

   var KEY_USER_ID: String
        set(value) = preference.edit().putString(_KEY_USER_ID, value).apply()
        get() = preference.getString(_KEY_USER_ID, "")!!

    var KEY_USER_NAME: String
        set(value) = preference.edit().putString(_KEY_USER_NAME, value).apply()
        get() = preference.getString(_KEY_USER_NAME, "")!!

    var KEY_USER_TYPE: Int
        set(value) = preference.edit().putInt(_KEY_USER_TYPE, value).apply()
        get() = preference.getInt(_KEY_USER_NAME, 0)!!

    private fun updateResources(context: Context, language: String): Context {
        var context = context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return context
    }

}

