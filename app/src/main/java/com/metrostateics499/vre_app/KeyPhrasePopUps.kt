package com.metrostateics499.vre_app

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment

class KeyPhrasePopUps(textViewSelected: String, buttonType: String) : AppCompatDialogFragment() {
    private val textViewSelected = textViewSelected
    private val buttonType = buttonType

    private var editTextKeyphrase: EditText? = null
    private var listener: Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater

        when (buttonType) {
            "edit" -> {
                val view = inflater.inflate(R.layout.layout_edit_text_popup, null)
                var textView: TextView = view.findViewById(R.id.edit_keyphrase)
                textView.text = textViewSelected
                builder.setView(view)
                    .setTitle("Edit Key Phrase")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        val keyphraseString = editTextKeyphrase!!.text.toString()
                        listener!!.editKeyPhrase(keyphraseString)
                    }
                editTextKeyphrase = view.findViewById(R.id.edit_keyphrase)
                //            return builder.create()
            }
            "add" -> {
                val view = inflater.inflate(R.layout.layout_edit_text_popup, null)
                builder.setView(view)
                    .setTitle("New Key Phrase")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        val keyphraseString = editTextKeyphrase!!.text.toString()
                        listener!!.addKeyPhrase(keyphraseString)
                    }
                editTextKeyphrase = view.findViewById(R.id.edit_keyphrase)
                //            return builder.create()
            }
            "delete" -> {
                val view = inflater.inflate(R.layout.layout_delete_popup, null)
                var textView: TextView = view.findViewById(R.id.text_keyphrase_delete)
                textView.text = textViewSelected
                builder.setView(view)
                    .setTitle("Are you sure you want to delete this key phrase?")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        listener!!.deleteKeyPhrase(textViewSelected)
                    }
            }
        }
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as Listener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString() +
                    "must implement ExampleDialogListener"
            )
        }
    }

    interface Listener {
        fun editKeyPhrase(keyphraseString: String)
        fun deleteKeyPhrase(keyphraseString: String)
        fun addKeyPhrase(keyphraseString: String)
    }
}