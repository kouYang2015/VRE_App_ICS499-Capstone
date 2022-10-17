package com.example.vre_app

import android.system.Os.remove
import com.example.vre_app.KeyPhrase
import com.example.vre_app.KeyPhraseList
import java.util.*

class KeyPhraseList
/**
 * Singleton KeyPhraseList
 */
private constructor() {
    var keyPhrases: MutableList<KeyPhrase> = LinkedList()
    fun addKeyPhrase(keyPhrase: KeyPhrase): Boolean {
        keyPhrases.add(keyPhrase)
        return true
    }
    fun deleteKeyPhrase(keyPhrase: KeyPhrase): Boolean {
        for(item in keyPhrases){
            if (item.keyPhrase == keyPhrase.keyPhrase){
                keyPhrases.remove(item)
                break
            }
        }
        return true
    }

    override fun toString(): String {
        return keyPhrases.toString()
    } //

    //    public List getKeyPhrases(){
    //        return keyPhrases;
    //    }
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