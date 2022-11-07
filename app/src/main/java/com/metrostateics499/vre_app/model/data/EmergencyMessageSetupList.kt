package com.metrostateics499.vre_app.model.data

import com.metrostateics499.vre_app.model.data.EmergencyMessageSetup

data class EmergencyMessageSetupList(
    var emergencyMessageSetups:
        MutableList<EmergencyMessageSetup>
) {

    fun addEmergencyMessageSetup(emergencyMessageSetup: EmergencyMessageSetup): Boolean {
        for (item in emergencyMessageSetups) {
            if (item.title == emergencyMessageSetup.title) {
                return false
            }
        }
        emergencyMessageSetups.add(emergencyMessageSetup)
        return true
    }

    fun editEmergencyMessageSetup(
        emergencyMessageSetup: EmergencyMessageSetup,
        editedEmergencyMessageSetup: EmergencyMessageSetup
    ): Boolean {
        var targetEmergencyMessageSetup: EmergencyMessageSetup? = null
        if (emergencyMessageSetup.title == editedEmergencyMessageSetup.title) {
            return false
        }
        for (item in emergencyMessageSetups) {
            if (item.title == emergencyMessageSetup.title)
                targetEmergencyMessageSetup = item
            if (item.title == editedEmergencyMessageSetup.title) {
                return false
            }
        }
        return if (targetEmergencyMessageSetup != null) {
            targetEmergencyMessageSetup = editedEmergencyMessageSetup
            true
        } else {
            false
        }
    }

    fun deleteEmergencyMessageSetup(emergencyMessageSetupTitle: String): Boolean {
        for (item in emergencyMessageSetups) {
            if (item.title == emergencyMessageSetupTitle) {
                emergencyMessageSetups.remove(item)
                break
            }
        }
        return true
    }

    fun findEmergencyMessageSetup(emergencyMessageSetupTitle: String): EmergencyMessageSetup? {
        var targetEmergencyMessageSetup: EmergencyMessageSetup? = null
        for (item in emergencyMessageSetups) {
            if (item.title == emergencyMessageSetupTitle) {
                targetEmergencyMessageSetup = item
                break
            }
        }
        return targetEmergencyMessageSetup
    }

    /**
     * To string
     *
     * @return keyPhrases
     */
    override fun toString(): String {
        return emergencyMessageSetups.toString()
    }

    fun editEmergencyMessageSetupKeyPhrase(
        textViewSelected: String,
        customTextString: String
    ): Boolean {
        var targetEmergencyMessageSetup: EmergencyMessageSetup? = null
        if (textViewSelected == customTextString) {
            return false
        }
        for (item in emergencyMessageSetups) {
            if (item.keyPhrase.keyPhrase == textViewSelected)
                targetEmergencyMessageSetup = item
            if (item.keyPhrase.keyPhrase == customTextString) {
                return false
            }
        }
        return if (targetEmergencyMessageSetup != null) {
            targetEmergencyMessageSetup.keyPhrase.keyPhrase = customTextString
            true
        } else {
            false
        }
    }

    fun editEmergencyMessageSetupTitle(
        textViewSelected: String,
        customTextString: String
    ): Boolean {
        var targetEmergencyMessageSetup: EmergencyMessageSetup? = null
        if (textViewSelected == customTextString) {
            return false
        }
        for (item in emergencyMessageSetups) {
            if (item.title == textViewSelected)
                targetEmergencyMessageSetup = item
            if (item.title == customTextString) {
                return false
            }
        }
        return if (targetEmergencyMessageSetup != null) {
            targetEmergencyMessageSetup.title = customTextString
            true
        } else {
            false
        }
    }

    fun editEmergencyMessageSetupCustomTextMessage(
        textViewSelected: String,
        customTextString: String
    ): Boolean {
        var targetEmergencyMessageSetup: EmergencyMessageSetup? = null
        if (textViewSelected == customTextString) {
            return false
        }
        for (item in emergencyMessageSetups) {
            if (item.customTextMessage.customTextMessage == textViewSelected)
                targetEmergencyMessageSetup = item
            if (item.customTextMessage.customTextMessage == customTextString) {
                return false
            }
        }
        return if (targetEmergencyMessageSetup != null) {
            targetEmergencyMessageSetup.customTextMessage.customTextMessage = customTextString
            true
        } else {
            false
        }
    }
}