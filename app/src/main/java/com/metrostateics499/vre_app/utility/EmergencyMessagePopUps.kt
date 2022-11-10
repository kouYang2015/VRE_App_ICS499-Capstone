package com.metrostateics499.vre_app.utility

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.metrostateics499.vre_app.R

class EmergencyMessagePopUps(
    private val textViewSelected: String,
    private val buttonType: String
) : AppCompatDialogFragment() {

    private var editText: EditText? = null
    private var listener: Listener? = null

    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater

        when (buttonType) {
            "edit" -> {
                val view = inflater.inflate(R.layout.activity_edit_emergency_message, null)
                val textView: TextView = view.findViewById(R.id.text_view_em)
                textView.text = textViewSelected
                builder.setView(view)
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("save") { dialogInterface, i ->
                        listener!!.refreshList()
                    }
            }

            "add" -> {
                val view = inflater.inflate(R.layout.layout_edit_text3_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                val textView2: TextView = view.findViewById(R.id.edit_text2)
                val textView3: TextView = view.findViewById(R.id.edit_text3)
                textView.hint = "Enter Emergency Message Title"
                textView2.hint = "Enter Key Phrase"
                textView3.hint = "Enter Custom Text Message"
                builder.setView(view)
                    .setTitle("New Emergency Message")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("next") { dialogInterface, i ->
                        val titleName = textView.text.toString()
                        val keyPhrase = textView2.text.toString()
                        val customText = textView3.text.toString()

                        listener!!.addEmergencyMessageSetup(titleName, keyPhrase, customText)
                    }
                editText = view.findViewById(R.id.edit_text)
            }

            "delete" -> {
                val view = inflater.inflate(R.layout.layout_delete_popup, null)
                val textView: TextView = view.findViewById(R.id.text_view_popup)
                textView.text = textViewSelected
                builder.setView(view)
                    .setTitle("Are you sure you want to delete this custom text message?")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        listener!!.deleteEmergencyMessageSetup(textViewSelected)
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
        fun deleteEmergencyMessageSetup(customTextString: String)
        fun addEmergencyMessageSetup(titleName: String, keyPhrase: String, customText: String)
        fun goToEditPage()
        fun refreshList()
    }
}
