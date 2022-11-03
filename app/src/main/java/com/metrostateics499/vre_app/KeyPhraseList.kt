package com.metrostateics499.vre_app

import java.util.*

class KeyPhraseList
/**
 * Singleton
 * Key phrase list
 *
 * @constructor Create empty Key phrase list
 */
private constructor() {
    var keyPhrases: MutableList<KeyPhrase> = LinkedList()

    /**
     * Add key phrase
     *
     * @param keyPhrase
     * @return boolean
     */
    fun addKeyPhrase(keyPhrase: KeyPhrase): Boolean {
        for (item in keyPhrases) {
            if (item.keyPhrase == keyPhrase.keyPhrase) {
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
        if(keyPhrase.toString() == editedKeyPhrase){
            return false
        }
        for (item in keyPhrases) {

            if (item.keyPhrase == editedKeyPhrase) {
                return false
            }
        }
        for (item in keyPhrases) {
            if (item.keyPhrase == keyPhrase.keyPhrase) {
                item.keyPhrase = editedKeyPhrase
                return true
            }
        }
        return false
    }

    /**
     * Delete key phrase
     *
     * @param keyPhrase
     * @return boolean
     */
    fun deleteKeyPhrase(keyPhrase: KeyPhrase): Boolean {
        for (item in keyPhrases) {
            if (item.keyPhrase == keyPhrase.keyPhrase) {
                keyPhrases.remove(item)
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
        return keyPhrases.toString()
    }

    /**
     * Companion
     * Instantiates the KeyPhraseList if it doesn't exist and returns the list
     *
     * @constructor Create empty Companion
     * @return keyPhraseList
     */
    companion object {
        private var keyPhraseList: KeyPhraseList? = null
        val instance: KeyPhraseList?
            get() {
                if (keyPhraseList == null) {
                    keyPhraseList = KeyPhraseList()
                }
                return keyPhraseList
            }
    }
}