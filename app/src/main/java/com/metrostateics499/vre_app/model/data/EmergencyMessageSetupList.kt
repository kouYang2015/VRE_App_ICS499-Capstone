package com.metrostateics499.vre_app.model.data

data class EmergencyMessageSetupList(
    var emergencyMessageSetups:
        MutableList<EmergencyMessageSetup>
) {

    fun addEmergencyMessageSetup(emergencyMessageSetup: EmergencyMessageSetup): Boolean {
        for (item in emergencyMessageSetups) {
            if (item.title.equals(emergencyMessageSetup.title, true)) {
                return false
            }
        }
        emergencyMessageSetups.add(emergencyMessageSetup)
        return true
    }

    fun deleteEmergencyMessageSetup(emergencyMessageSetupTitle: String): Boolean {
        for (item in emergencyMessageSetups) {
            if (item.title.equals(emergencyMessageSetupTitle, true)) {
                emergencyMessageSetups.remove(item)
                break
            }
        }
        return true
    }

    fun findEmergencyMessageSetup(emergencyMessageSetupTitle: String): EmergencyMessageSetup? {
        var targetEmergencyMessageSetup: EmergencyMessageSetup? = null
        for (item in emergencyMessageSetups) {
            if (item.title.equals(emergencyMessageSetupTitle, true)) {
                targetEmergencyMessageSetup = item
                break
            }
        }
        return targetEmergencyMessageSetup
    }

    fun checkKeyPhraseDuplicate(
        keyPhrase: String,
    ): Boolean {
        for (item in emergencyMessageSetups) {
            if (item.keyPhrase.keyPhrase.equals(keyPhrase, true)) {
                return true
            }
        }
        return false
    }

    fun editEmergencyMessageSetupKeyPhrase(
        textViewSelected: String,
        customTextString: String
    ): Boolean {
        var targetEmergencyMessageSetup: EmergencyMessageSetup? = null
        if (textViewSelected.equals(customTextString, true)) {
            return false
        }
        for (item in emergencyMessageSetups) {
            if (item.keyPhrase.keyPhrase.equals(textViewSelected, true))
                targetEmergencyMessageSetup = item
            if (item.keyPhrase.keyPhrase.equals(customTextString, true)) {
                return false
            }
        }
        return if (targetEmergencyMessageSetup != null) {
            targetEmergencyMessageSetup.keyPhrase.keyPhrase = (customTextString)
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
        if (textViewSelected.equals(customTextString, true)) {
            return false
        }
        for (item in emergencyMessageSetups) {
            if (item.title.equals(textViewSelected, true))
                targetEmergencyMessageSetup = item
            if (item.title.equals(customTextString, true)) {
                return false
            }
        }
        return if (targetEmergencyMessageSetup != null) {
            targetEmergencyMessageSetup.title = (customTextString)
            true
        } else {
            false
        }
    }

    /**
     * Edit emergency message setup custom text message
     * This may not be needed here because we don't care about duplicates in the list.
     * The object can use it's own editCustomTextMessage
     *
     * @param textViewSelected
     * @param customTextString
     * @return
     */
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