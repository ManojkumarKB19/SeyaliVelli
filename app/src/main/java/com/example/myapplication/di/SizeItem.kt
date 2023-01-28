package com.example.myapplication.di

import com.google.gson.annotations.SerializedName

data class SizeItem(
    @SerializedName("Length")
    var length:String? = null,
    @SerializedName("Weight")
    var weight:String? = null,
    @SerializedName("model_size")
    var model_size:Boolean = false,
    var size_key:String? = null,
    var isSelected:Boolean = false,
    var quantities:Int = 0
)
