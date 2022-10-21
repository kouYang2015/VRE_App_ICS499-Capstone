package com.metrostateics499.vre_app

// Tried to save username and password (First Time login-in)
object Passing {
    var username: String = ""
    var password: String = ""
    var keyPhraseList = KeyPhraseList.instance
    var email: String = ""

    @JvmName("setUsername1")
    fun setUsername(username: String) {
        username.also { Passing.username = it }
    }

    @JvmName("setEmail1")
    fun setEmail(email: String) {
        email.also { Passing.email = it }
    }

    @JvmName("setPassword1")
    fun setPassword(password: String) {
        password.also { Passing.password = it }
    }

    /**
     * Add key phrase
     *
     * @param keyPhrase
     */
    @JvmName("createKeyPhraseList")
    fun addKeyPhrase(keyPhrase: KeyPhrase) {
        keyPhraseList?.addKeyPhrase(keyPhrase)
    }
}