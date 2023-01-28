package com.example.myapplication.phoneAuth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityPhoneAuthBinding
import com.example.myapplication.utils.Constant.INTENT_NAME
import com.example.myapplication.utils.Constant.INTENT_PHONE_NUMBER
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhoneAuthActivity:AppCompatActivity() {

    private lateinit var binding: ActivityPhoneAuthBinding

   // val request: GetPhoneNumberHintIntentRequest = GetPhoneNumberHintIntentRequest.builder().build()
   // val intent = Credentials.getClient(this).getHintPickerIntent(hintRequest)

    var isToPickFromPhone = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.txtLeftToggle.setOnClickListener {
            toggleSelection(true)
           /* binding.txtPhoneEdtxt.setOnClickListener {
                requestHint()
            }*/
        }

        binding.txtRightToggle.setOnClickListener {
            toggleSelection(false)
        }

        binding.txtContinue.setOnClickListener {

            val number: String = binding.txtPhoneEdtxt.getText().toString().trim().takeLast(10)
            val name: String = binding.txtEdtxName.text.toString().trim()

            if(name.isEmpty()){
                binding.txtName.setError(getString(R.string.txt_name_required_error))
                binding.txtName.requestFocus()
            }else if (number.isEmpty() || number.length < 10) {
                binding.txtPhoneEdtxt.setError(getString(R.string.txt_phone_number_required))
                binding.txtPhoneEdtxt.requestFocus()
            }else{
                //val code: String = CountryData.countryAreaCodes.get(spinner.getSelectedItemPosition())
                val code: String = "91"

                val phoneNumber = "+$code$number"

                val intent = Intent(this, VerifyPhoneActivity::class.java)
                intent.putExtra(INTENT_PHONE_NUMBER, phoneNumber)
                intent.putExtra(INTENT_NAME, name)
                startActivity(intent)
            }
        }
    }

    private fun showToast(txt:String){
        Toast.makeText(this,txt,Toast.LENGTH_LONG).show()
    }

   /* val phoneNumberHintIntentResultLauncher: ActivityResultLauncher<Intent> =  registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
            try {
                val phoneNumber = Identity.getSignInClient(this).getPhoneNumberFromIntent(result.data)
                showToast("phone number ${phoneNumber}")
            } catch(e: Exception) {
                Log.e("PhoneAuthActivity", "Phone Number Hint failed")
            }
        }*/


    /*fun getHint(){
        val phoneNumberHintIntentResultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
        ) {
            try {
                val phoneNumber = Identity.getSignInClient(context)
                    .getPhoneNumberFromIntent(it.data)
                println("phoneNumber $phoneNumber")
            } catch (e: Exception) {
                println("Phone Number Hint failed")
                e.printStackTrace()
            }

        }

    }*/

    private val phoneNumberHintIntentResultLauncher: ActivityResultLauncher<IntentSenderRequest> = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val phoneNumber = Identity.getSignInClient(this).getPhoneNumberFromIntent(result.data)
            //showToast("phoneNumber $phoneNumber")
            binding.txtPhoneEdtxt.setText(phoneNumber.takeLast(10))
        } catch (e: Exception) {
            Log.d("PhoneAuthActivity", "phone auth failed ")

        }
    }

    // Construct a request for phone numbers and show the picker
    private fun requestHint() {
       /* val hintRequest: HintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val intent: PendingIntent = Auth.CredentialsApi.getHintPickerIntent(
            apiClient, hintRequest
        )
        startIntentSenderForResult(
            intent.intentSender,
            RESOLVE_HINT, null, 0, 0, 0
        )*/

       /* val phoneNumberHintIntentResultLauncher: ActivityResultLauncher<IntentSenderRequest> = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            try {
                Identity.getSignInClient(this).getPhoneNumberHintIntent(request)
                val phoneNumber = Identity.getSignInClient(this).getPhoneNumberFromIntent(result.data)
                showToast(phoneNumber +" is selected")
                // Do something with the number
                    /*.addOnSuccessListener {
                        try {
                            showToast("")
                        }catch (e:Exception){
                            Log.e("PhoneAuthActivity", "Launching the PendingIntent failed")
                        }
                    }*/
            }catch (e:Exception){
                Log.e("PhoneAuthActivity", "Phone Number Hint failed")

            }
        }
        Identity.getSignInClient(this)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener {
            try{
                phoneNumberHintIntentResultLauncher.launch(request.getIntentSender())
            }catch (e:Exception){

            }
        }

        phoneNumberHintIntentResultLauncher.launch(request.)
*/

       /* Identity.getSignInClient(this)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener {
                try {
                    phoneNumberHintIntentResultLauncher.launch(it.intentSender.sendIntent())
                } catch(e: Exception) {
                    Log.e("PhoneAuthActivity", "Launching the PendingIntent failed")
                }
            }
            .addOnFailureListener {
                Log.e("PhoneAuthActivity","phone number hint failed")
            }*/

        val request: GetPhoneNumberHintIntentRequest = GetPhoneNumberHintIntentRequest.builder().build()

        Identity.getSignInClient(this)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener {
                phoneNumberHintIntentResultLauncher.launch(
                    IntentSenderRequest.Builder(it.intentSender).build()
                )
            }
            .addOnFailureListener {
                Log.d("phoneAuth", it.message.toString())
            }

    }

    fun toggleSelection(isLeft:Boolean){
        if(isLeft){
            isToPickFromPhone = true
            binding.txtLeftToggle.backgroundTintList = this.resources.getColorStateList(R.color.white,null)
            binding.txtRightToggle.backgroundTintList = this.resources.getColorStateList(R.color.silver,null)
            //binding.txtPhoneEdtxt.isFocusable = false
        }else{
            requestHint()
            isToPickFromPhone  = false
            binding.txtLeftToggle.backgroundTintList = this.resources.getColorStateList(R.color.silver,null)
            binding.txtRightToggle.backgroundTintList = this.resources.getColorStateList(R.color.white,null)
           // binding.txtPhoneEdtxt.isFocusable = true
        }
    }
}