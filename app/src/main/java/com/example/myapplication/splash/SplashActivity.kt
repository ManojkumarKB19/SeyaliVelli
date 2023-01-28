package com.example.myapplication.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.phoneAuth.PhoneAuthActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity:AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    var databaseReference: DatabaseReference? = null
    var firebaseDatabase: FirebaseDatabase? = null

    private val sh: SharedPreferences? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

      /*  binding.splashOne.animate().translationY(-1600.00f).setDuration(1000).setStartDelay(6000)
        binding.txtAppName.animate().translationY(1400f).setDuration(1000).startDelay = 6000*/

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ checkUserSignedInFirebase() }, 2000)
    }

    private fun checkUserSignedInFirebase() {
        val mUser = FirebaseAuth.getInstance().currentUser
        if (mUser != null) {
            mUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result.token
                        val user = mAuth!!.currentUser
                        startMainActivity()

                        //getUserType(user)
                    } else {
                        // Handle error -> task.getException();
                        startLoginActivity()
                    }
                }
        } else {
            startLoginActivity()
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, PhoneAuthActivity::class.java)
        startActivity(intent)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /* private fun getUserType(user: FirebaseUser) {
        databaseReference!!.child(user.uid).child(Constant.INTENT_USER_TYPE).get()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("firebase", "Error getting data", task.exception)
                } else {
                    Log.d("firebase", "user type const " + task.result.value.toString())
                    startMainActivity(task.result.value.toString())
                }
            }
    }*/


   /* private fun startMainActivity(userTypeConst: String) {
        if (userTypeConst.contains(AppConstants.CONST_VAL_ADMIN_TYPE)) {
            val intent: Intent = Intent(this, MainActivity::class.java).putExtra(
                AppConstants.INTENT_USER_TYPE,
                AppConstants.CONST_VAL_ADMIN_TYPE
            )
            startActivity(intent)
        } else if (userTypeConst.contains(CONST_VAL_STAFF_TYPE)) {
            val intent: Intent = Intent(this, MainActivity::class.java).putExtra(
                AppConstants.INTENT_USER_TYPE,
                CONST_VAL_STAFF_TYPE
            )
            startActivity(intent)
        } else {
            val intent: Intent = Intent(this, MainActivity::class.java).putExtra(
                AppConstants.INTENT_USER_TYPE,
                CONST_VAL_STUDENT_TYPE
            )
            startActivity(intent)
        }
    }*/
}