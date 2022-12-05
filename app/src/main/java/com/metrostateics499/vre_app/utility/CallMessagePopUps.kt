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

class CallMessagePopUps(
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
                textView.text = Passing.selectedCallMessageObject.title
                textView2.text = Passing.selectedCallMessageObject.callMessage
                builder.setView(view)
                    .setTitle("Edit Call Message")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        val customTextTitle = textView.text.toString().trim()
                        val customTextMessage = textView2.text.toString().trim()
                        listener!!.editCallMessage(customTextTitle, customTextMessage)
                    }
                editText = view.findViewById(R.id.edit_text)
            }

            "add" -> {
                val view = inflater.inflate(R.layout.layout_edit_text2_popup, null)
                val textView: TextView = view.findViewById(R.id.edit_text)
                val textView2: TextView = view.findViewById(R.id.edit_text2)
                textView.hint = "Title (e.g. Name, Address, Medical..)"
                textView2.hint = "Custom Call Message"
                builder.setView(view)
                    .setTitle("New Custom Call Message")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        val customTextTitle = textView.text.toString().trim()
                        val customTextMessage = textView2.text.toString().trim()
                        listener!!.addCallMessage(customTextTitle, customTextMessage)
                    }
                editText = view.findViewById(R.id.edit_text)
            }

            "delete" -> {
                val view = inflater.inflate(R.layout.layout_delete_popup, null)
                val textView: TextView = view.findViewById(R.id.text_view_popup)
                textView.text = Passing.selectedCallMessageObject.title +
                    ": " + Passing.selectedCallMessageObject.callMessage
                builder.setView(view)
                    .setTitle("Are you sure you want to delete this call message?")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("ok") { dialogInterface, i ->
                        listener!!.deleteCallMessage(textViewSelected)
                    }
            }
            "listenEntireCallMessage" -> {
                val view = inflater.inflate(R.layout.layout_text_view_popup, null)
                val textView: TextView = view.findViewById(R.id.text)
                textView.text = Passing.selectedEmergencyMessageSetup.getCallMessageListString()
                builder.setView(view)
                    .setTitle("Listening to Call Message")
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
        fun editCallMessage(callMessageTitle: String, callMessageString: String)
        fun deleteCallMessage(callMessageString: String)
        fun addCallMessage(callMessageTitle: String, callMessageString: String)
    }
}
