package com.example.myapplication.di

import com.google.gson.annotations.SerializedName

data class DesignListDataModel(
    var Name:String = "",
    var ImageUrl:String = "",
    var Size:HashMap<String,Any>? = null,
    @SerializedName("Average Weight")
    var weight:String = "",
    var Key:String = ""
)