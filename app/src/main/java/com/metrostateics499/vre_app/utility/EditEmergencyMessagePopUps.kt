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

class EditEmergencyMessagePopUps(
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
                val title = (Passing.selectedEmergencyMessageSetup.title)
                textView.text = title

                builder.setView(view)
                    .setTitle("Edit Title")
                    .setNegativeButton("cancel") { dialogInterface, i -> }
                    .setPositiveButton("save") { dialogInterface, i ->
                        val inputTitle = editText!!.text.toString().trim()
                        listener!!.editEmergencyMessageSetupTitle(inputTitle)
                    }
                editText = view.findViewById(R.id.edit_text)
            }
            "menuGPSTrackingInactive" -> {
                val view = inflater.inflate(R.layout.layout_text_view_popup, null)
                val textView: TextView = view.findViewById(R.id.text)
                textView.text = "You must also activate GPS tracking in " +
                    "the Main Menu in order for your current gps coordinates to be " +
                    "sent with your emergency messages"

                builder.setView(view)
                    .setTitle("Alert")
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
        fun editEmergencyMessageSetupTitle(inputTitle: String)
    }
}