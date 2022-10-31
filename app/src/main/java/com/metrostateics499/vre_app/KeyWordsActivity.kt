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

    private var textViewSelected: String = ""
    private var textViewSelectedBoolean: Boolean = false
    private var viewSelected: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.key_phrases_menu)
        refreshList()
    }

    private fun refreshList() {
        textViewSelectedBoolean = false
        textViewSelected = ""
//        viewSelected?.setBackgroundResource(androidx.appcompat.R.drawable.abc_item_background_holo_light)

        val listview: ListView = findViewById<ListView>(R.id.listViewPhrases)
        var arrayAdapter = Passing.keyPhraseList?.let {
            ArrayAdapter(
                this, android.R.layout.simple_list_item_1, it.keyPhrases
            )
        }
        listview.adapter = arrayAdapter
        listview.setOnItemClickListener { parent, view, position, id ->
            viewSelected?.setBackgroundResource(androidx.appcompat.R.drawable.abc_item_background_holo_light)
            Toast.makeText(this@KeyWordsActivity, "You have selected " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show()
            textViewSelected = parent.getItemAtPosition(position).toString()
            viewSelected = view
            textViewSelectedBoolean = true
            view.setBackgroundResource(androidx.appcompat.R.drawable.abc_list_pressed_holo_dark)
        }
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
        refreshList()
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
//    fun selectKeyPhrase(view: View) {
//        var textView: TextView = findViewById(R.id.textViewPhrase)
//        textViewSelected = textView.text.toString()
//        println("textViewSelected $textViewSelected")
//    }

    fun goToDeleteKeyPhraseConfirmation(view: View) {
        if (textViewSelectedBoolean) {
            setContentView(R.layout.delete_key_phrase_confirm)
            var textView: TextView = findViewById(R.id.deleteKeyPhraseTextView)
            textView.setText(textViewSelected)
        } else {
            Toast.makeText(this@KeyWordsActivity, "You must select a key phrase first", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Delete key phrase
     *
     * @param view
     */
    fun deleteKeyPhrase(view: View) {
        if (textViewSelectedBoolean) {
            var textViewSelectedThis: String = textViewSelected
            println("calling delete key phrase with phrase: $textViewSelected")
            if (textViewSelected?.isNotEmpty() == true) {
                Passing.keyPhraseList?.deleteKeyPhrase(KeyPhrase(textViewSelectedThis))
                Toast.makeText(this@KeyWordsActivity, "You have deleted phrase: " + textViewSelected, Toast.LENGTH_SHORT).show()
            }
            goToKeyPhraseMenu(view)
        }
    }

    fun goToEditKeyPhraseMenu(view: View) {
        if (textViewSelectedBoolean) {
            setContentView(R.layout.edit_key_phrase)
            var editText: EditText = findViewById(R.id.editTextKeyPhraseEdit)
            editText.setText(textViewSelected)
//            textViewSelected = findViewById(R.id.editTextKeyPhraseEdit).text.toString()
        } else {
            Toast.makeText(this@KeyWordsActivity, "You must select a key phrase first", Toast.LENGTH_SHORT).show()
        }
    }

    fun editKeyPhrase(view: View) {
        if (textViewSelectedBoolean) {
            var editText: EditText = findViewById(R.id.editTextKeyPhraseEdit)
            var editTextKeyPhraseEdit: String = editText.text.toString()
            if (editTextKeyPhraseEdit.isNotEmpty()) {
                Passing.keyPhraseList?.editKeyPhrase(
                    KeyPhrase(textViewSelected),
                    editTextKeyPhraseEdit
                )
            }
            goToKeyPhraseMenu(view)
        }
    }
}