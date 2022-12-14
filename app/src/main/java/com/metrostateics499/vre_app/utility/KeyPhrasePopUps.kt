package com.metrostateics499.vre_app.utility

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.metrostateics499.vre_app.R

class KeyPhrasePopUps(
    private val textViewSelected: String,
    private val buttonType: String
) : AppCompatDialogFragment() {

    private var editTextKeyphrase: EditText? = null
    private var listener: Listener? = null

    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater

        when (buttonType) {
            "edit" -> {
                val view = inflater.inflate(R.layout.layout_edit_text_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                textView.text = textViewSelected
                builder.setView(view)
                    .setTitle("Edit Key Phrase")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        val keyphraseString = editTextKeyphrase!!.text.toString().trim()
                        listener!!.editKeyPhrase(keyphraseString)
                    }
                editTextKeyphrase = view.findViewById(R.id.edit_text)
            }
            "add" -> {
                val view = inflater.inflate(R.layout.layout_edit_text_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                textView.hint = "Key Phrase"
                builder.setView(view)
                    .setTitle("New Key Phrase")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        val keyphraseString = editTextKeyphrase!!.text.toString().trim()
                        listener!!.addKeyPhrase(keyphraseString)
                    }
                editTextKeyphrase = view.findViewById(R.id.edit_text)
            }
            "delete" -> {
                val view = inflater.inflate(R.layout.layout_delete_popup, null)
                val textView: TextView = view.findViewById(R.id.text_view_popup)
                textView.text = textViewSelected
                builder.setView(view)
                    .setTitle("Are you sure you want to delete this key phrase?")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        listener!!.deleteKeyPhrase()
                    }
            }
            "success" -> {
                val view = inflater.inflate(R.layout.layout_text_view_popup, null)
                val textView: TextView = view.findViewById(R.id.text)
                textView.text = textViewSelected
                textView.setTextColor(Color.parseColor("#A81B1B"))
                builder.setView(view)
                    .setTitle("Success! Recognized Speech: \n")
                    .setNegativeButton("done") { dialogInterface, i -> }
            }
            "unrecognized" -> {
                val view = inflater.inflate(R.layout.layout_text_view_popup, null)
                val textView: TextView = view.findViewById(R.id.text)
                textView.text = "Try again, or try something else"
                builder.setView(view)
                    .setTitle("Could not recognize speech with that key phrase \n")
                    .setNegativeButton("Done") { dialogInterface, i -> }
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
        fun deleteKeyPhrase()
        fun addKeyPhrase(keyphraseString: String)
    }
}