package com.example.myapplication.phoneAuth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage

class SmsReceiver:BroadcastReceiver() {

    private var mListener: SmsListener? = null
    var b: Boolean? = null
    var abcd: String? = null
    var xyz:kotlin.String? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val data = intent!!.extras
        val pdus = data!!["pdus"] as Array<Any>?
        for (i in 0..pdus?.size!!) {

            val smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SmsMessage.createFromPdu(pdus[i] as ByteArray, "")
            } else {
                SmsMessage.createFromPdu(pdus[i] as ByteArray)
            }
            val sender = smsMessage.displayOriginatingAddress
            // b=sender.endsWith("WNRCRP");  //Just to fetch otp sent from WNRCRP
            val messageBody = smsMessage.messageBody
            abcd = messageBody.replace("[^0-9]", ""); // here abcd contains otp
            //which is in number format
            //Pass on the text to our listener.
            if (b == true) {
                mListener?.messageReceived(abcd); // attach value to interface   object
            } else {
            }
        }
    }
    fun bindListener(listener: SmsListener) {
        mListener = listener
    }

}


