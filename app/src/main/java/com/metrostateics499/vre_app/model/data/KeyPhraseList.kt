package com.metrostateics499.vre_app.model.data

data class KeyPhraseList(var keyPhrases: MutableList<KeyPhrase>) {

    /**
     * Add key phrase
     *
     * @param keyPhrase
     * @return boolean
     */
    fun addKeyPhrase(keyPhrase: KeyPhrase): Boolean {
        for (item in keyPhrases) {
            if (item.keyPhrase.equals(keyPhrase.keyPhrase, true)) {
                return false
            }
        }
        keyPhrases.add(keyPhrase)
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
    fun editKeyPhrase(keyPhrase: KeyPhrase, editedKeyPhrase: String): Boolean {
        var targetKeyPhrase: KeyPhrase? = null
        if (keyPhrase.toString().equals(editedKeyPhrase, true)) {
            return false
        }
        for (item in keyPhrases) {
            if (item.keyPhrase.equals(keyPhrase.keyPhrase, true))
                targetKeyPhrase = item
            if (item.keyPhrase.equals(editedKeyPhrase, true)) {
                return false
            }
        }
        return if (targetKeyPhrase != null) {
            editedKeyPhrase.trim()
            targetKeyPhrase.keyPhrase = editedKeyPhrase
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
    fun deleteKeyPhrase(keyPhrase: KeyPhrase): Boolean {
        for (item in keyPhrases) {
            if (item.keyPhrase.equals(keyPhrase.keyPhrase, true)) {
                keyPhrases.remove(item)
                break
            }
        }
        return true
    }
}