package com.metrostateics499.vre_app.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.KeyPhrase
import com.metrostateics499.vre_app.utility.KeyPhrasePopUps

/**
 * Key words activity
 * This class is the Activity for the user to interact with the key phrases menu
 * It also initiates functions to add and remove key phrases
 *
 * @constructor Create empty Key words activity
 */
class KeyPhraseActivity : AppCompatActivity(), KeyPhrasePopUps.Listener {

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

        val listview: ListView = findViewById(R.id.listViewPhrases)
        val arrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, Passing.keyPhraseList.keyPhrases
        )
        listview.adapter = arrayAdapter
        listview.setOnItemClickListener { parent, view, position, id ->
            viewSelected?.setBackgroundResource(
                androidx.appcompat.R.drawable
                    .abc_item_background_holo_light
            )
            Toast.makeText(
                this@KeyPhraseActivity,
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
     * Not currently used by might be needed later
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
                this@KeyPhraseActivity, "You must select a key phrase first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSelectForEditKeyPhrasePopUp() {
        if (textViewSelectedBoolean) {
            openPopUp(textViewSelected, "edit")
        } else {
            Toast.makeText(
                this@KeyPhraseActivity, "You must select a key phrase first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openPopUp(textViewSelected: String, buttonType: String) {
        val keyPhrasePopUp = KeyPhrasePopUps(textViewSelected, buttonType)
        keyPhrasePopUp.show(supportFragmentManager, "example dialog")
    }

    override fun addKeyPhrase(keyphraseString: String) {
        if (keyphraseString.isEmpty()) {
            Toast.makeText(
                this@KeyPhraseActivity,
                "Please enter a key phrase",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        } else if (keyphraseString.isNotEmpty() &&
            Passing.keyPhraseList.addKeyPhrase(KeyPhrase(keyphraseString))
        ) {
            Toast.makeText(
                this@KeyPhraseActivity,
                "New Key Phrase Successfully " +
                    "Added",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        } else {
            Toast.makeText(
                this@KeyPhraseActivity,
                "That Key Phrase already exists. Try something else or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        }
    }

    override fun editKeyPhrase(keyphraseString: String) {
        if (keyphraseString.isEmpty()) {
            Toast.makeText(
                this@KeyPhraseActivity,
                "Key phrase can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (keyphraseString == textViewSelected) {
            Toast.makeText(
                this@KeyPhraseActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (keyphraseString.isNotEmpty() && Passing.keyPhraseList.editKeyPhrase(
                KeyPhrase(textViewSelected),
                keyphraseString
            )
        ) {
            Toast.makeText(
                this@KeyPhraseActivity, "Successfully Edited",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        } else {
            Toast.makeText(
                this@KeyPhraseActivity,
                "That Key Phrase already exists. " +
                    "Try something else or click cancel.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        }
    }

    override fun deleteKeyPhrase(keyphraseString: String) {
        if (textViewSelected.isNotEmpty()) {
            Passing.keyPhraseList.deleteKeyPhrase(KeyPhrase(textViewSelected))
            Toast.makeText(
                this@KeyPhraseActivity,
                "You have deleted phrase: " +
                    textViewSelected,
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        }
    }
}