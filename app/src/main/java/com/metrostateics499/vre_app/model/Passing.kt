package com.metrostateics499.vre_app.model

import java.util.*

// Tried to save username and password (First Time login-in)
object Passing {
    var username: String = ""
    var password: String = ""
    var email: String = ""

    private var newKeyPhraseList: MutableList<KeyPhrase> = LinkedList()
    var keyPhraseList = KeyPhraseList(newKeyPhraseList)

    private var newCustomTextMessageListList: MutableList<CustomTextMessage> = LinkedList()
    var customTextMessageList = CustomTextMessageList(newCustomTextMessageListList)

    private var newEmergencyMessageSetupListList: MutableList<EmergencyMessageSetup> = LinkedList()
    var emergencyMessageSetupList = EmergencyMessageSetupList(newEmergencyMessageSetupListList)

    var selectedEmergencyMessageSetup: EmergencyMessageSetup? = null

    @JvmName("setUsername1")
    fun setUsername(username: String) {
        username.also { Passing.username = it }
    }

    @JvmName("setEmail1")
    fun setEmail(email: String) {
        email.also { Passing.email = it }
    }

    @JvmName("setPassword1")
    fun setPassword(password: String) {
        password.also { Passing.password = it }
    }

    /**
     * Add key phrase
     *
     * @param keyPhrase
     */
    @JvmName("createKeyPhraseList")
    fun addKeyPhrase(keyPhrase: KeyPhrase) {
        keyPhraseList.addKeyPhrase(keyPhrase)
    }
}