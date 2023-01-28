package com.example.myapplication.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.util.LocalHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication:Application() {

    var localeHelper: LocalHelper? = null

    companion object{
        lateinit var appController:MyApplication

        @Synchronized
        fun getInstanse(): MyApplication? {
            return appController
        }
    }

    override fun onCreate() {
        super.onCreate()

        appController = this
    }

    override fun attachBaseContext(base: Context?) {
        localeHelper = LocalHelper(base!!)
        super.attachBaseContext(localeHelper!!.setLocale(base!!))
    }

   /* override fun attachBaseContext(base: Context?) {
        localeHelper = LocalHelper(base!!)
        var pref = getSharedPreferences("Settings", MODE_PRIVATE)
        var lang = pref.getString("My_Lang","")

        super.attachBaseContext(localeHelper!!.setLocale(base,lang!!))
    }*/
}