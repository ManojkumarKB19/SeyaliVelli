package com.example.myapplication.dao

import android.util.Log
import com.example.myapplication.di.DesignListDataModel
import com.example.myapplication.util.LocalHelper
import com.example.myapplication.utils.Constant
import com.google.firebase.database.DatabaseReference
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

    fun placeOrder(designItem: DesignListDataModel){
        val  database = Firebase.database.reference
        var userId = LocalHelper._KEY_USER_ID
        database.child(userId).child("orders").push().setValue(designItem)

    }

}