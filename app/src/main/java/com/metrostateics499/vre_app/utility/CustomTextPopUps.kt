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

class CustomTextPopUps(
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
                textView.text = Passing.selectedCustomTextObject.title
                textView2.text = Passing.selectedCustomTextObject.textMessage
                builder.setView(view)
                    .setTitle("Edit Text Message")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        val customTextTitle = textView.text.toString().trim()
                        val customTextMessage = textView2.text.toString().trim()
                        listener!!.editCustomTextMessage(customTextTitle, customTextMessage)
                    }
                editText = view.findViewById(R.id.edit_text)
            }

            "add" -> {
                val view = inflater.inflate(R.layout.layout_edit_text2_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                val textView2: TextView = view.findViewById(R.id.edit_text2)
                textView.hint = "Title (e.g. Name, Address, Medical..)"
                textView2.hint = "Custom Text Message"
                builder.setView(view)
                    .setTitle("New Custom Text Message")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        val customTextTitle = textView.text.toString().trim()
                        val customTextMessage = textView2.text.toString().trim()
                        listener!!.addCustomTextMessage(customTextTitle, customTextMessage)
                    }
                editText = view.findViewById(R.id.edit_text)
            }

            "delete" -> {
                val view = inflater.inflate(R.layout.layout_delete_popup, null)
                val textView: TextView = view.findViewById(R.id.text_view_popup)
                textView.text = Passing.selectedCustomTextObject.title +
                    ": " + Passing.selectedCustomTextObject.textMessage
                builder.setView(view)
                    .setTitle("Are you sure you want to delete this custom text message?")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        listener!!.deleteCustomTextMessage(textViewSelected)
                    }
            }
            "viewEntireText" -> {
                val view = inflater.inflate(R.layout.layout_text_view_popup, null)
                val textView: TextView = view.findViewById(R.id.text)
                textView.text = Passing.selectedEmergencyMessageSetup.getCustomTextListString()
                builder.setView(view)
                    .setPositiveButton("ok") { dialogInterface, i ->
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
        fun editCustomTextMessage(customTextTitle: String, customTextString: String)
        fun deleteCustomTextMessage(customTextString: String)
        fun addCustomTextMessage(customTextTitle: String, customTextString: String)
    }
}
