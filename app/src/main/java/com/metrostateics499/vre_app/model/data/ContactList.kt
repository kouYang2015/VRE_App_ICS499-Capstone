package com.metrostateics499.vre_app.model.data

data class ContactList(var contacts: MutableList<Contact>) {
    fun getContactListNames(): String {
        var nameString = ""
        for (item in contacts)
            nameString += item.name

        return nameString
    }

    fun addContact(contact: Contact): Boolean {
        for (item in contacts) {
            if (item.name == contact.name) {
                return false
            }
        }
        contacts.add(contact)
        return true
    }

    fun findContact(contactName: String): Contact? {
        var targetContact: Contact? = null
        for (contactElement in contacts) {
            if (contactElement.name == contactName) {
                targetContact = contactElement
                break
            }
        }
        return targetContact
    }

    fun editContact(contact: Contact, name: String, phone: String): Boolean {
        var targetContact: Contact? = null
        if (contact.name == name) {
            contact.phoneNumber = phone
            return true
        }
        for (item in contacts) {
            if (item.name == contact.name)
                targetContact = item
            if (item.name == name) {
                return false
            }
        }
        return if (targetContact != null) {
            targetContact.name = name
            targetContact.phoneNumber = phone
            true
        } else {
            false
        }
    }

    fun deleteContact(selectedContact: Contact?): Boolean {
        if (contacts.remove(selectedContact)) {
            return true
        }
        return false
    }
}