package com.metrostateics499.vre_app.model.data

data class EmergencyMessageSetup(
    var title: String,
    var keyPhrase: KeyPhrase,
    var customTextMessage: CustomTextMessage,
    var selectedContactList: MutableList<Contact>
) {
    fun addContact(contact: Contact) {
        selectedContactList.add(contact)
    }

    fun getContactListNames(): String {
        var nameString = ""
        for(item in selectedContactList)
            nameString += (item.name + ", ")

        return nameString
    }

    fun removeContact(contact: Contact) {
        selectedContactList.remove(contact)
    }
    fun findContact(contactName: String): Boolean {
        var targetContact: Contact? = null
        for (item in selectedContactList) {
            if (item.name == contactName) {
                targetContact = item
                return true
            }
        }
        return false
    }

}
