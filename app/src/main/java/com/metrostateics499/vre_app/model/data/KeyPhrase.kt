package com.metrostateics499.vre_app.model.data

/**
 * Key phrase
 * The KeyPhrase object
 *
 * @property phrase
 * @constructor Create empty Key phrase
 */
data class KeyPhrase(var phrase: String, var inUse: Boolean = false) {
    override fun toString(): String {
        return phrase
    }
}