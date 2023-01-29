package com.example.myapplication.dao

import android.util.Log
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.Constant.ORDER
import com.example.myapplication.utils.Constant.USER
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


object DesignDao {

    fun get(ky:String):Query{
        val  database = Firebase.database.reference
        Log.d("DesignDao","The key "+ky)

        if(ky.isEmpty()){
            Log.d("DesignDao","startAt Key "+"empty")
            return database.child(Constant.PARAM_ITEMS).child(Constant.PARAM_ANKLETS).child("S Design").orderByKey().limitToFirst(5)
        }else{
            Log.d("DesignDao","startAt Key "+ky)
            return  database.child(Constant.PARAM_ITEMS).child(Constant.PARAM_ANKLETS).child("S Design").orderByKey().startAfter(ky).limitToFirst(5)
        }
    }

    fun getOrders(ky:String,userId:String):Query{
        val  database = Firebase.database.reference
        Log.d("DesignDao","The user id "+userId)

        if(ky.isEmpty()){
            Log.d("DesignDao","startAt Key "+"empty")
            return database.child(USER).child(userId).child(ORDER).orderByKey().limitToFirst(5)
        }else{
            Log.d("DesignDao","startAt Key "+userId)
            return database.child(USER).child(userId).child(ORDER).orderByKey().startAfter(ky).limitToFirst(5)
        }
    }



}