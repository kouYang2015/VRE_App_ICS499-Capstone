package com.metrostateics499.vre_app.model.data

data class ContactList(var contacts: MutableList<Contact>) {

    fun addContact(contact: Contact): Boolean {
        for (item in contacts) {
            if (item.name.equals(contact.name, true)) {
                return false
            }
        }
        contacts.add(contact)
        return true
    }

    fun editContact(contact: Contact, name: String, phone: String): Boolean {
        var targetContact: Contact? = null
        if (contact.name.equals(name, true)) {
            contact.phoneNumber = phone
            return true
        }
        for (item in contacts) {
            if (item.name.equals(contact.name, true))
                targetContact = item
            if (item.name.equals(name, true)) {
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

    fun deleteContact(selectedContact: Contact?,): Boolean {
        if (contacts.remove(selectedContact)) {
            return true
        }
        return false
    }

    fun findContact(contactName: String): Contact? {
        var targetContact: Contact? = null
        for (item in contacts) {
            if (item.name == contactName) {
                targetContact = item
                break
            }
        }
        return targetContact
    }
}