package com.metrostateics499.vre_app.model.data

data class EmergencyMessageSetup(
    var title: String,
    var selectedKeyPhraseList: MutableList<KeyPhrase>,
    var selectedCustomTextMessages: MutableList<CustomTextMessage>,
    var selectedCallMessages: MutableList<CallMessage>,
    var selectedContactList: MutableList<Contact>,
    var activeEMS: Boolean = false,
    var activeGPS: Boolean = false,
    var activeSendText: Boolean = false,
    var activeCall: Boolean = false
) {
    fun addContact(contact: Contact) {
        selectedContactList.add(contact)
    }

    fun removeContact(contact: Contact) {
        selectedContactList.remove(contact)
    }

    fun getContactListNames(): String {
        var nameString = ""
        for (item in selectedContactList)
            nameString += (item.name + ", ")

        return nameString
    }

    fun getKeyPhraseListString(): String {
        var nameString = ""
        for (item in selectedKeyPhraseList)
            nameString += (item.phrase + ", ")

        return nameString
    }

    fun getCustomTextListString(): String {
        var nameString = ""
        for (item in selectedCustomTextMessages)
            nameString += (item.title + ": " + item.textMessage + "; ")

        return nameString
    }

    fun getCallMessageListString(): String {
        var nameString = ""
        for (item in selectedCallMessages)
            nameString += (item.title + ": " + item.callMessage + "; ")

        return nameString
    }

    fun findContactObject(contact: Contact): Boolean {
        for (item in this.selectedContactList)
            if (item == contact) {
                return true
            }
        return false
    }

//    fun useKeyPhrase(keyPhrase: KeyPhrase) {
//
//    }
}
