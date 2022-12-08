package com.metrostateics499.vre_app.model

import com.metrostateics499.vre_app.model.data.*

/**
 * Singleton object that is used to store user data.
 *
 * @constructor Create empty Passing singleton object.
 */
object Passing {

    var fullName: String = ""
    var username: String = ""
    var password: String = ""
    var email: String = ""
    var phoneNumber: String = ""

    var keyPhraseList: MutableList<KeyPhrase> = mutableListOf()
    var customTextMessageList: MutableList<CustomTextMessage> = mutableListOf()
    var contactList: MutableList<Contact> = mutableListOf()
    var emergencyMessageSetupList: MutableList<EmergencyMessageSetup> = mutableListOf()
    var callMessageList: MutableList<CallMessage> = mutableListOf()

    lateinit var selectedEmergencyMessageSetup: EmergencyMessageSetup
    lateinit var selectedContactObject: Contact
    lateinit var selectedKeyPhraseObject: KeyPhrase
    lateinit var selectedCustomTextObject: CustomTextMessage
    lateinit var selectedCallMessageObject: CallMessage

    lateinit var vreActivatedEMS: EmergencyMessageSetup
    var vreServiceActive = false
    var locationTrackingRequested = false
    var latitude: String = "Unknown"
    var longitude: String = "Unknown"
    var dateTimeGPS: String = "Unknown"
    var callingInProcess: Boolean = false
    var deactivateCallingPhrase: String = "Cancel Calling"
    var callNextContactPhrase: String = "Call Next Contact"

    @JvmName("setFullName1")
    fun setFullName(fullName: String) {
        fullName.also { Passing.fullName = it }
    }

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

    @JvmName("setPhoneNumber1")
    fun setPhoneNumber(phone: String) {
        phone.also { Passing.phoneNumber = it }
    }

    fun checkInitializationSelectedEmergencyMessageSetup(): Boolean {
        return ::selectedEmergencyMessageSetup.isInitialized
    }

    fun checkInitializationSelectedKeyPhrase(): Boolean {
        return ::selectedKeyPhraseObject.isInitialized
    }

    fun checkInitializationSelectedCustomTextMessage(): Boolean {
        return ::selectedCustomTextObject.isInitialized
    }
}