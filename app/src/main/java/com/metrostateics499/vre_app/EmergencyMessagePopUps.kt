package com.metrostateics499.vre_app

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.activity_edit_emergency_message.*

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
//                    .setTitle("Edit " + this.textViewSelected)
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("save") { dialogInterface, i ->
//                        val customTextString = editTextCustomTextMessage!!.text.toString()
                        listener!!.refreshList()
                    }
//                editTextCustomTextMessage = view.findViewById(R.id.edit_text)
                //            return builder.create()
            }
            "add" -> {
                val view = inflater.inflate(R.layout.layout_edit_text_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                textView.hint = "Enter Emergency Message Title"
                builder.setView(view)
                    .setTitle("New Title")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("next") { dialogInterface, i ->
                        val emergencyMessageString = editText!!.text.toString()
                        listener!!.addEmergencyMessageSetup(emergencyMessageString)
                        listener!!.goToEditPage()
                    }
                editText = view.findViewById(R.id.edit_text)
                //            return builder.create()
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
        fun editEmergencyMessageSetup(customTextString: String)
        fun deleteEmergencyMessageSetup(customTextString: String)
        fun addEmergencyMessageSetup(customTextString: String)
        fun goToEditPage()
        fun refreshList()
    }
}
