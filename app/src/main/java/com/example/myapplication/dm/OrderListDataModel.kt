package com.example.myapplication.dm

import com.google.gson.annotations.SerializedName

data class OrderListDataModel(
    var Name:String = "",
    var ImageUrl:String = "",
    var Size:HashMap<String,Any>? = null,
    @SerializedName("Average Weight")
    var weight:String = "",
    var Key:String = ""
)
