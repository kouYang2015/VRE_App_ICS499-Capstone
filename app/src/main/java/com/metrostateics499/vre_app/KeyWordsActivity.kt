package com.metrostateics499.vre_app

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

/**
 * Key words activity
 * This class is the Activity for the user to interact with the key phrases menu
 * It also initiates functions to add and remove key phrases
 *
 * @constructor Create empty Key words activity
 */
class KeyWordsActivity : AppCompatActivity() {
    private var textViewSelected: String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.key_phrases_menu)

        val listview: ListView = findViewById<ListView>(R.id.listViewPhrases)
        var arrayAdapter = Passing.keyPhraseList?.let {
            ArrayAdapter(
                this, R.layout.text_view, it.keyPhrases
            )
        }
        listview.adapter = arrayAdapter
    }

    /**
     * Go to key phrase menu
     * This is used by the buttons that navigate to the key phrases menu
     * It also refreshes the key phrases list every time it is used
     *
     * @param view
     */
    fun goToKeyPhraseMenu(view: View) {
        setContentView(R.layout.key_phrases_menu)

        val listview: ListView = findViewById<ListView>(R.id.listViewPhrases)
        var arrayAdapter = Passing.keyPhraseList?.let {
            ArrayAdapter(
                this, R.layout.text_view, it.keyPhrases
            )
        }
        listview.adapter = arrayAdapter
    }

    /**
     * Go to add key phrase menu
     * This is used by the Add button and brings you to the add
     * a key phrase layout
     *
     * @param view
     */
    fun goToAddKeyPhraseMenu(view: View) {
        setContentView(R.layout.add_key_phrase)
    }

    /**
     * Add key phrase
     * This function is used in the add a key phrase layout
     *
     * @param view
     */
    fun addKeyPhrase(view: View) {
        var editText: EditText = findViewById(R.id.editTextKeyPhrase)
        var editTextKeyPhrase: String = editText.text.toString()
        if (editTextKeyPhrase.isNotEmpty()) {
            Passing.addKeyPhrase(KeyPhrase(editTextKeyPhrase))
        }
        goToKeyPhraseMenu(view)
    }

    /**
     * Select key phrase
     * This allows the user to select a key phrase which will
     * let the user delete them
     *
     * @param view
     */
    fun selectKeyPhrase(view: View) {
        var textView: TextView = findViewById(R.id.textViewPhrase)
        textViewSelected = textView.text.toString()
        println("textViewSelected $textViewSelected")
    }

    /**
     * Delete key phrase
     *
     * @param view
     */
    fun deleteKeyPhrase(view: View) {
        var textViewSelectedThis: String = textViewSelected
        println("calling delete key phrase with phrase: $textViewSelected")
        if (textViewSelected?.isNotEmpty() == true) {
            Passing.keyPhraseList?.deleteKeyPhrase(KeyPhrase(textViewSelectedThis))
            goToKeyPhraseMenu(view)
        }
    }
}