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
    var activeCall: Boolean = false,
    var activeAudioWarningMessage: Boolean = false,
    var activePingLocation: Boolean = false
) {
    fun getContactListNames(): String {
        var nameString = ""
        for (item in selectedContactList)
            nameString += if (item != selectedContactList.last()) {
                (item.name + ", ")
            } else {
                (item.name)
            }

        return nameString
    }

    fun getKeyPhraseListString(): String {
        var nameString = ""
        for (item in selectedKeyPhraseList) {
            nameString += if (item != selectedKeyPhraseList.last()) {
                (item.phrase + ", ")
            } else {
                (item.phrase)
            }
        }

        return nameString
    }

    fun getCustomTextListString(): String {
        var nameString = ""
        for (item in selectedCustomTextMessages) {
            nameString += if (item != selectedCustomTextMessages.last()) {
                (item.title + ": " + item.textMessage + "; ")
            } else {
                (item.title + ": " + item.textMessage)
            }
        }

        return nameString
    }

    fun getCallMessageListString(): String {
        var nameString = ""
        for (item in selectedCallMessages) {
            nameString += if (item != selectedCallMessages.last()) {
                (item.title + ": " + item.callMessage + "; ")
            } else {
                (item.title + ": " + item.callMessage)
            }
        }

        return nameString
    }
}
