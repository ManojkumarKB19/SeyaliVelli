package com.example.myapplication.phoneAuth

interface SmsListener {
    fun messageReceived(messageText: String?)
}