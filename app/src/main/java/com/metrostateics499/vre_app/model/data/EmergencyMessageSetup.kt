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

    fun removeContact(contact: Contact) {
        selectedContactList.remove(contact)
    }

    fun getContactListNames(): String {
        var nameString = ""
        for (item in selectedContactList)
            nameString += (item.name + ", ")

        return nameString
    }

    fun editEmergencyMessageSetupCustomTextMessage(
        textViewSelected: String,
        customTextString: String
    ): Boolean {
        return if (textViewSelected.equals(customTextString, true)) {
            false
        } else if (this.customTextMessage.customTextMessage.equals(customTextString, true)) {
            false
        } else
            run {
                this.customTextMessage.customTextMessage = customTextString
                true
            }
    }

    fun findContactObject(contact: Contact): Boolean {
        for (item in this.selectedContactList)
            if (item == contact) {
                return true
            }
        return false
    }
}
