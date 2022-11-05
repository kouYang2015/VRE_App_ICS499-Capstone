package com.metrostateics499.vre_app

data class CustomTextMessageList(var customTextMessages: MutableList<CustomTextMessage>) {

    /**
     * Add key phrase
     *
     * @param customTextMessage
     * @return boolean
     */
    fun addCustomTextMessage(customTextMessage: CustomTextMessage): Boolean {
        for (item in customTextMessages) {
            if (item.customTextMessage == customTextMessage.customTextMessage) {
                return false
            }
        }
        customTextMessages.add(customTextMessage)
        return true
    }

    /**
     * Edit key phrase
     * can duplicate a keyphrase through editing because there is no ID to identify
     * the key phrase by
     *
     * @param keyPhrase
     * @param editedKeyPhrase
     * @return
     */
    fun editCustomTextMessage(customTextMessage: CustomTextMessage,
                              editedCustomTextMessage: String): Boolean {
        var targetCustomTextMessage: CustomTextMessage? = null
        if (customTextMessage.toString() == editedCustomTextMessage) {
            return false
        }
        for (item in customTextMessages) {
            if (item.customTextMessage == customTextMessage.customTextMessage)
                targetCustomTextMessage = item
            if (item.customTextMessage == editedCustomTextMessage) {
                return false
            }
        }
        return if (targetCustomTextMessage != null) {
            targetCustomTextMessage.customTextMessage = editedCustomTextMessage
            true
        } else {
            false
        }
    }

    /**
     * Delete key phrase
     *
     * @param keyPhrase
     * @return boolean
     */
    fun deleteCustomTextMessage(customTextMessage: CustomTextMessage): Boolean {
        for (item in customTextMessages) {
            if (item.customTextMessage == customTextMessage.customTextMessage) {
                customTextMessages.remove(item)
                break
            }
        }
        return true
    }

    /**
     * To string
     *
     * @return keyPhrases
     */
    override fun toString(): String {
        return customTextMessages.toString()
    }
}