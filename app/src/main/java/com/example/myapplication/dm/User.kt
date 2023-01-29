package com.example.myapplication.dm

import com.example.myapplication.utils.Constant
import com.google.firebase.database.Exclude

/*public class User : Serializable {

    var userTypeConst: String? = null
    var userName: String? = null
    var userPhoneNumber: String? = null

}*/

data class User(
    var userName: String? = null,
    var userPhoneNumber: String? = null,
    var userTypeConst: Int? = null
)


