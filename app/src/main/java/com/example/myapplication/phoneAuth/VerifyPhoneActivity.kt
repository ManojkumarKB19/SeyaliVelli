package com.example.myapplication.phoneAuth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityVerifyPhoneAuthBinding
import com.example.myapplication.dm.User
import com.example.myapplication.util.GenericTextWatcher
import com.example.myapplication.util.LocalHelper
import com.example.myapplication.utils.Constant
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class VerifyPhoneActivity:AppCompatActivity() {

    private lateinit var binding: ActivityVerifyPhoneAuthBinding

    private val verificationId: String? = null
    private var mAuth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var editText: EditText? = null

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    var phoneNumber:String? = null
    var userName:String? = null
    var userType:Int? = 0

    @Inject
    lateinit var localHelper: LocalHelper

    var firebaseDatabase: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseDatabase = FirebaseDatabase.getInstance()

        binding = ActivityVerifyPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        phoneNumber = intent.getStringExtra(Constant.INTENT_PHONE_NUMBER)
        binding.txtMobileNumber.setText(phoneNumber)

        userName = intent.getStringExtra(Constant.INTENT_NAME)
        userType = Constant.USER_TYPE_CONST_USER


        /*This is important because this will be called every time you receive
             any sms */
        /*This is important because this will be called every time you receive
             any sms */

        /*SmsReceiver().bindListener(object : SmsListener {
            override fun messageReceived(messageText: String?) {
                binding.editTextCode.setText(messageText)
            }
        })*/

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                binding.progressbar.visibility = View.GONE
                Log.d("VerifyPhoneActivity", "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                binding.progressbar.visibility = View.GONE
                Log.w("VerifyPhoneActivity", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("VerifyPhoneActivity", "onCodeSent:$verificationId")

                binding.progressbar.visibility = View.GONE
                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }
        }
        // [END phone_auth_callbacks]


        sendVerificationCode(phoneNumber!!)

        val edit = arrayOf<EditText>(binding.otpEditBox1, binding.otpEditBox2, binding.otpEditBox3, binding.otpEditBox4,binding.otpEditBox5,binding.otpEditBox6)

        binding.otpEditBox1.addTextChangedListener(GenericTextWatcher(binding.otpEditBox1, edit))
        binding.otpEditBox2.addTextChangedListener(GenericTextWatcher(binding.otpEditBox2, edit))
        binding.otpEditBox3.addTextChangedListener(GenericTextWatcher(binding.otpEditBox3, edit))
        binding.otpEditBox4.addTextChangedListener(GenericTextWatcher(binding.otpEditBox4, edit))
        binding.otpEditBox5.addTextChangedListener(GenericTextWatcher(binding.otpEditBox5, edit))
        binding.otpEditBox6.addTextChangedListener(GenericTextWatcher(binding.otpEditBox6, edit))


        binding.buttonSignIn.setOnClickListener{

            var otp = binding.otpEditBox1.text.toString() + binding.otpEditBox2.text.toString() + binding.otpEditBox3.text.toString() + binding.otpEditBox4.text.toString() + binding.otpEditBox5.text.toString() + binding.otpEditBox6.text.toString()
            val code = otp.trim { it <= ' ' }

            if (code.isEmpty() || code.length < 6) {
                editText!!.error = "Enter code..."
                editText!!.requestFocus()
            }
            verifyCode(code)
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithCredential(credential)
    }


    private fun signInWithCredential(credential: PhoneAuthCredential) {
        binding.progressbar.visibility = View.VISIBLE
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                binding.progressbar.visibility = View.GONE
                if (task.isSuccessful) {
                    val user_uid = task.result?.user?.uid
                   // showToast("signInWithCredential:success ${user.toString()}")

                    if(!user_uid.isNullOrEmpty()){
                        val user = User(userName, phoneNumber,userType)
                        updateUser(user,user_uid)
                    }else{
                        showToast(getString(R.string.txt_something_went_wrong))
                    }

                } else {
                    Toast.makeText(
                        this@VerifyPhoneActivity,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun updateUser(postValues: User, uid:String) {
        firebaseDatabase!!.reference.child(Constant.USER).child(uid).setValue(
            postValues
        ) { error, ref ->
            Toast.makeText(
                this,
                "user info successfully inserted",
                Toast.LENGTH_SHORT
            ).show()

            Log.d("VerifyPhoneActivity",uid)
            localHelper.KEY_USER_ID = uid
            localHelper.KEY_USER_NAME = postValues.userName!!
            localHelper.KEY_USER_TYPE = userType?:2


            val intent = Intent(this@VerifyPhoneActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    private fun sendVerificationCode(number: String) {
        binding.progressbar.visibility = View.VISIBLE

        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(number)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        binding.progressbar.visibility = View.VISIBLE
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                binding.progressbar.visibility = View.GONE
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    /*Log.d("VerifyPhoneActivity", "signInWithCredential:success")

                    val user = task.result?.user
                    showToast("signInWithCredential:success ${user.toString()}")

                    val intent = Intent(this@VerifyPhoneActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)*/
                    val user_uid = task.result?.user?.uid
                    // showToast("signInWithCredential:success ${user.toString()}")

                    if(!user_uid.isNullOrEmpty()){
                        val user = User(userName, phoneNumber,userType)
                        updateUser(user,user_uid)
                    }else{
                        showToast(getString(R.string.txt_something_went_wrong))
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("VerifyPhoneActivity", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]


    private fun showToast(txt:String){
        Toast.makeText(this,txt,Toast.LENGTH_LONG).show()
    }

}