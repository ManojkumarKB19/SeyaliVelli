package com.example.myapplication.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.di.CategoryModel
import com.example.myapplication.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    private  var database: DatabaseReference
    private lateinit var ctgoryLst:ArrayList<CategoryModel>
    var categoryListLiveData : MutableLiveData<ArrayList<CategoryModel>> = MutableLiveData()

    init {
        database = Firebase.database.reference
        this.getCategoryLstValueListener()
        Log.d("HomeViewModel","init called")
    }


    fun getCategoryList(): LiveData<ArrayList<CategoryModel>>{
        // val myUserId = uid
        val myTopPostsQuery = database.child(Constant.PARAM_ITEMS)
        var lstCategory:ArrayList<CategoryModel>  = ArrayList()

        // My top posts by number of stars
        myTopPostsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val parent: String = postSnapshot.getKey().let { it }?:""
                    Log.i("HomeViewModel", parent)
                    lstCategory.add(CategoryModel(parent))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("HomeViewModel", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
        // [END basic_query_value_listener]
        ctgoryLst = lstCategory
        categoryListLiveData.apply {
            value =  ctgoryLst
        }
        return categoryListLiveData
    }


    fun getCategoryLstValueListener() {
       // val myUserId = uid
        val myTopPostsQuery = database.child(Constant.PARAM_ITEMS)
        var lstCategory:ArrayList<CategoryModel>  = ArrayList()

        // [START basic_query_value_listener]
        // My top posts by number of stars
        myTopPostsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val parent: String = postSnapshot.getKey().let { it }?:""
                    Log.i("HomeViewModel", parent)
                    lstCategory.add(CategoryModel(parent))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("HomeViewModel", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
        // [END basic_query_value_listener]
        ctgoryLst = lstCategory
    }

   private val categoryList = MutableLiveData<ArrayList<CategoryModel>>().apply {
        value = ctgoryLst
    }
    val categoryLst: MutableLiveData<ArrayList<CategoryModel>> = categoryList


}