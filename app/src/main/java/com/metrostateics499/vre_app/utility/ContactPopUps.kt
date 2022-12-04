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
import com.metrostateics499.vre_app.model.Passing

class ContactPopUps(
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
                val view = inflater.inflate(R.layout.layout_edit_text2_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                val textView2: TextView = view.findViewById(R.id.edit_text2)
                textView.text = Passing.selectedContactObject.name
                textView2.text = Passing.selectedContactObject.phoneNumber
                builder.setView(view)
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("save") { dialogInterface, i ->

                        val contactName = textView.text.toString().trim()
                        val contactPhone = textView2.text.toString().trim()
                        listener!!.editContact(contactName, contactPhone)
                    }
                editText = view.findViewById(R.id.edit_text)
            }

            "add" -> {
                val view = inflater.inflate(R.layout.layout_edit_text2_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                val textView2: TextView = view.findViewById(R.id.edit_text2)
                textView.hint = "Name"
                textView2.hint = "Phone Number"
                builder.setView(view)
                    .setTitle("New Contact")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        val contactName = textView.text.toString().trim()
                        val contactPhone = textView2.text.toString().trim()
                        listener!!.addContact(contactName, contactPhone)
                    }
                editText = view.findViewById(R.id.edit_text)
            }

            "delete" -> {
                val view = inflater.inflate(R.layout.layout_delete_popup, null)
                val textView: TextView = view.findViewById(R.id.text_view_popup)
                textView.text = textViewSelected
                builder.setView(view)
                    .setTitle("Are you sure you want to delete this contact?")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        listener!!.deleteContact(textViewSelected)
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
        fun editContact(contactName: String, contactPhone: String)
        fun deleteContact(contactName: String)
        fun addContact(contactName: String, contactPhone: String)
        fun refreshList()
    }
}