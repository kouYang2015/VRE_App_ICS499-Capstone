package com.metrostateics499.vre_app.model.data

data class EmergencyMessageSetup(
    var title: String,
    var selectedKeyPhraseList: MutableList<KeyPhrase>,
    var customTextMessage: CustomTextMessage,
    var selectedContactList: MutableList<Contact>,
    var activeEMS: Boolean = false
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
