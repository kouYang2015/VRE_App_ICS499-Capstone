package com.example.vre_app

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val keyPhraseList: KeyPhraseList? = KeyPhraseList.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createSampleListKeyPhrases()
    }

    fun goToKeyPhraseMenu(view: View) {
        setContentView(R.layout.key_phrases_menu)

        val listview: ListView = findViewById<ListView>(R.id.listViewPhrases)
        var arrayAdapter = keyPhraseList?.let {
            ArrayAdapter(
                this, R.layout.text_view, it.keyPhrases
            )
        }
        listview.adapter = arrayAdapter

//        val textView = findViewById<ListView>(R.id.listViewPhrases)
//        textView.setOnClickListener(View.OnClickListener {
//            textViewPhraseToDelete = textView.toString()
//        })


//        textView.setOnClickListener { val textViewPhraseToDelete = textView }

//        val textViewClicked = findViewById<ListView>(R.id.listViewPhrases) as ListView


//        val textViewClicked = R.layout.text_view
//        println("textViewClicked" + textViewClicked)
//        textViewClicked.setOnClickListener() {
//            textViewToDelete = textViewClicked.toString()
//        }

    }
    fun goToAddKeyPhraseMenu(view: View) {
        setContentView(R.layout.add_key_phrase)
    }
    fun goToMainMenu(view: View) {
        setContentView(R.layout.activity_main)
    }

    fun createSampleListKeyPhrases(){
        keyPhraseList?.addKeyPhrase(KeyPhrase("monkey"))
        keyPhraseList?.addKeyPhrase(KeyPhrase("monkey1"))
        keyPhraseList?.addKeyPhrase(KeyPhrase("monkey2"))
        keyPhraseList?.addKeyPhrase(KeyPhrase("monkey3"))
        keyPhraseList?.addKeyPhrase(KeyPhrase("monkey4"))
        keyPhraseList?.addKeyPhrase(KeyPhrase("monkey5"))
        keyPhraseList?.addKeyPhrase(KeyPhrase("monkey6"))
        keyPhraseList?.addKeyPhrase(KeyPhrase("monkey7"))

    }

    fun addKeyPhrase(view: View) {
        var editText: EditText = findViewById(R.id.editTextKeyPhrase)
        var editTextKeyPhrase: String = editText.text.toString()
        if (editTextKeyPhrase.isNotEmpty()) {
            keyPhraseList?.addKeyPhrase(KeyPhrase(editTextKeyPhrase))
        }
        goToKeyPhraseMenu(view)
    }

    fun selectKeyPhrase(view: View){
        var textView: TextView = findViewById(R.id.textViewPhrase)
        textViewSelected = textView.text.toString()
        println("textViewSelected $textViewSelected")
    }


    private var textViewSelected: String = "null"

    fun deleteKeyPhrase(view: View) {
        var textViewSelectedThis: String = textViewSelected
        println("calling delete key phrase with phrase: $textViewSelected")
        if (textViewSelected?.isNotEmpty() == true) {
            keyPhraseList?.deleteKeyPhrase(KeyPhrase(textViewSelectedThis))
            goToKeyPhraseMenu(view)
        }
    }


}




