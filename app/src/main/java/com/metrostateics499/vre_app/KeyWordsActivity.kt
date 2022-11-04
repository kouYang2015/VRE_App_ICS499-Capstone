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
class KeyWordsActivity : AppCompatActivity(), KeyPhrasePopUps.Listener {

    private var buttonAdd: Button? = null
    private var buttonEdit: Button? = null
    private var buttonDelete: Button? = null
    private var textViewSelected: String = ""
    private var textViewSelectedBoolean: Boolean = false
    private var viewSelected: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_phrases_menu)

        buttonAdd = findViewById<View>(R.id.buttonAddKeyPhrase) as Button
        buttonAdd!!.setOnClickListener { openPopUp(textViewSelected, "add") }
        buttonEdit = findViewById<View>(R.id.buttonEditKeyPhrase) as Button
        buttonEdit!!.setOnClickListener { checkSelectForEditKeyPhrasePopUp() }
        buttonDelete = findViewById<View>(R.id.buttonDeleteKeyPhrase) as Button
        buttonDelete!!.setOnClickListener { checkSelectForDeleteKeyPhrasePopUp() }
        refreshList()
    }

    private fun refreshList() {
        textViewSelectedBoolean = false
        textViewSelected = ""

        val listview: ListView = findViewById<ListView>(R.id.listViewPhrases)
        var arrayAdapter = Passing.keyPhraseList?.let {
            ArrayAdapter(
                this, android.R.layout.simple_list_item_1, it.keyPhrases
            )
        }
        listview.adapter = arrayAdapter
        listview.setOnItemClickListener { parent, view, position, id ->
            viewSelected?.setBackgroundResource(
                androidx.appcompat.R.drawable
                    .abc_item_background_holo_light
            )
            Toast.makeText(
                this@KeyWordsActivity,
                "You have selected " +
                    parent.getItemAtPosition(position),
                Toast.LENGTH_SHORT
            ).show()
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
        setContentView(R.layout.activity_key_phrases_menu)
        refreshList()
    }

    private fun checkSelectForDeleteKeyPhrasePopUp() {
        if (textViewSelectedBoolean) {
            openPopUp(textViewSelected, "delete")
        } else {
            Toast.makeText(
                this@KeyWordsActivity, "You must select a key phrase first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSelectForEditKeyPhrasePopUp() {
        if (textViewSelectedBoolean) {
            openPopUp(textViewSelected, "edit")
        } else {
            Toast.makeText(
                this@KeyWordsActivity, "You must select a key phrase first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openPopUp(textViewSelected: String, buttonType: String) {
        val keyPhrasePopUp = KeyPhrasePopUps(textViewSelected, buttonType)
        keyPhrasePopUp.show(supportFragmentManager, "example dialog")
    }

    override fun addKeyPhrase(keyphraseString: String) {
        if (Passing.keyPhraseList?.addKeyPhrase(KeyPhrase(keyphraseString)) == true) {
            Toast.makeText(
                this@KeyWordsActivity,
                "New Key Phrase Successfully " +
                    "Added",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this@KeyWordsActivity,
                "That Key Phrase already exists. Try something else or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        }
        refreshList()
    }

    override fun editKeyPhrase(keyphraseString: String) {
        if (keyphraseString == textViewSelected) {
            Toast.makeText(
                this@KeyWordsActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (Passing.keyPhraseList?.editKeyPhrase(
                KeyPhrase(textViewSelected),
                keyphraseString
            ) == true
        ) {
            Toast.makeText(
                this@KeyWordsActivity, "Successfully Edited",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        } else {

            Toast.makeText(
                this@KeyWordsActivity,
                "That Key Phrase already exists. " +
                    "Try something else or click cancel.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        }
    }

    override fun deleteKeyPhrase(keyphraseString: String) {
        if (textViewSelected?.isNotEmpty() == true) {
            Passing.keyPhraseList?.deleteKeyPhrase(KeyPhrase(textViewSelected))
            Toast.makeText(
                this@KeyWordsActivity,
                "You have deleted phrase: " +
                    textViewSelected,
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        }
    }
}