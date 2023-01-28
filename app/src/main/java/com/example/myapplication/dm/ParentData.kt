package com.example.myapplication.dm

import android.provider.SyncStateContract
import com.example.myapplication.utils.Constant

data class ParentData(
    val parentTitle:String?=null,
    var type:Int = Constant.PARENT,
    var subList : MutableList<ChildData> = ArrayList(),
    var isExpanded:Boolean = false
)
