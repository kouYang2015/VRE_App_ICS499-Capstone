package com.metrostateics499.vre_app.model

/**
 * Singleton object that is used to store user data.
 *
 * @constructor Create empty Passing singleton object.
 */
object Passing {
    var username: String = ""
    var password: String = ""
    var email: String = ""

    var keyPhraseList: MutableList<KeyPhrase> = mutableListOf()
    var customTextMessageList: MutableList<CustomTextMessage> = mutableListOf()
    var contactList: MutableList<Contact> = mutableListOf()
    var emergencyMessageSetupList: MutableList<EmergencyMessageSetup> = mutableListOf()

    var selectedEmergencyMessageSetup: EmergencyMessageSetup? = null
    lateinit var selectedContactObject: Contact

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
}