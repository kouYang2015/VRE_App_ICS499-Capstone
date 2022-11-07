package com.metrostateics499.vre_app

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment

class EditEmergencyMessagePopUps(
//    private val textViewSelected: String,
    private val buttonType: String
) : AppCompatDialogFragment() {

    private var editText: EditText? = null
    private var listener: Listener? = null

    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater

        when (buttonType) {
            "title" -> {
                val view = inflater.inflate(R.layout.layout_edit_text_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                val title = (Passing.selectedEmergencyMessageSetup?.title ?: String)
                    as CharSequence?
                textView.text = title

                builder.setView(view)
                    .setTitle("Edit $title")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("save") { dialogInterface, i ->
                        val customTextString = editText!!.text.toString()
                        listener!!.editEmergencyMessageSetupTitle(customTextString)
                    }
                editText = view.findViewById(R.id.edit_text)
                //            return builder.create()
            }
            "keyphrase" -> {
                val view = inflater.inflate(R.layout.layout_edit_text_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                val keyPhrase = Passing.selectedEmergencyMessageSetup?.keyPhrase?.keyPhrase
                    ?: toString()
                textView.text = keyPhrase

                textView.hint = "Key Phrase"
                builder.setView(view)
                    .setTitle("Edit Key Phrase")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("save") { dialogInterface, i ->
                        val customTextString = editText!!.text.toString()
                        listener!!.editEmergencyMessageSetupKeyPhrase(customTextString)
//                        listener!!.goToEditPage()
                    }
                editText = view.findViewById(R.id.edit_text)
                //            return builder.create()
            }
            "customTextMessage" -> {
                val view = inflater.inflate(R.layout.layout_edit_text_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                val customTextMessage = Passing.selectedEmergencyMessageSetup?.customTextMessage
                    .toString()
                textView.text = customTextMessage

                textView.hint = "Custom Text Message"
                builder.setView(view)
                    .setTitle("Edit Custom Text Message")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("save") { dialogInterface, i ->
                        val customTextString = editText!!.text.toString()
                        listener!!.editEmergencyMessageSetupCustomTextMessage(customTextString)
//                        listener!!.goToEditPage()
                    }
                editText = view.findViewById(R.id.edit_text)
                //            return builder.create()
            }
//            "delete" -> {
//                val view = inflater.inflate(R.layout.layout_delete_popup, null)
//                val textView: TextView = view.findViewById(R.id.text_view_popup)
//                textView.text = textViewSelected
//                builder.setView(view)
//                    .setTitle("Are you sure you want to delete this custom text message?")
//                    .setNegativeButton("cancel") { dialogInterface, i -> }
//                    .setPositiveButton("ok") { dialogInterface, i ->
//                        listener!!.deleteEmergencyMessageSetup(textViewSelected)
//                    }
//            }
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
        fun editEmergencyMessageSetupTitle(customTextString: String)
        fun editEmergencyMessageSetupKeyPhrase(customTextString: String)
        fun editEmergencyMessageSetupCustomTextMessage(customTextString: String)
//        fun goToEditPage()
    }
}