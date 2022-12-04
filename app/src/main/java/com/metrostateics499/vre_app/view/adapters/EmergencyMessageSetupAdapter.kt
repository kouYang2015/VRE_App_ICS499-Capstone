package com.metrostateics499.vre_app.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.EmergencyMessageSetup
import kotlinx.android.synthetic.main.activity_edit_emergency_message.*
import kotlinx.android.synthetic.main.row.view.*

class EmergencyMessageSetupAdapter(
    private val mutableList: MutableList<EmergencyMessageSetup>,
    val context: Context
) : RecyclerView.Adapter<EmergencyMessageSetupAdapter.ViewHolder>() {

    var viewSelected: View? = null
    var titleSelectedString: String = ""
    var viewSelectedBoolean: Boolean = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(emergencyMessageSetup: EmergencyMessageSetup) {

            itemView.title.text = emergencyMessageSetup.title
            itemView.description.text = emergencyMessageSetup.getKeyPhraseListString()
            if (emergencyMessageSetup.activeEMS &&
                emergencyMessageSetup.selectedKeyPhraseList.isNotEmpty() &&
                emergencyMessageSetup.selectedContactList.isNotEmpty()
            ) {
                itemView.switch2.isChecked = true
                itemView.activeSwitchTextView.setTextColor(Color.parseColor("#1BB100"))
                itemView.activeSwitchTextView.text = "Active"
            } else {
                emergencyMessageSetup.activeEMS = false
                itemView.activeSwitchTextView.setTextColor(Color.parseColor("#B50909"))
                itemView.activeSwitchTextView.text = "Inactive"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(mutableList[position])
        holder.itemView.setOnClickListener {
            viewSelected?.setBackgroundResource(
                androidx.appcompat.R.drawable
                    .abc_item_background_holo_light
            )
            viewSelectedBoolean = false
            titleSelectedString = ""
            Passing.selectedEmergencyMessageSetup
            Toast.makeText(
                context,
                "You have selected " + mutableList[position].title,
                Toast.LENGTH_SHORT
            ).show()
            viewSelected = holder.itemView
            titleSelectedString = mutableList[position].title
            viewSelectedBoolean = true
            Passing.selectedEmergencyMessageSetup = mutableList[position]

            holder.itemView.setBackgroundResource(
                androidx.appcompat.R.drawable.abc_list_pressed_holo_dark
            )
        }
        holder.itemView.switch2.setOnClickListener {
            if (holder.itemView.switch2.isChecked &&
                Passing.selectedEmergencyMessageSetup.selectedContactList.isNotEmpty() &&
                Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isNotEmpty()
            ) {
                holder.itemView.activeSwitchTextView
                    .setTextColor(Color.parseColor("#1BB100"))
                holder.itemView.activeSwitchTextView.text = "Active"
                Passing.selectedEmergencyMessageSetup.activeEMS = true
                Toast.makeText(
                    context,
                    "You have activated VRE service for EMS: " + mutableList[position].title,
                    Toast.LENGTH_SHORT
                ).show()
            } else if (holder.itemView.switch2.isChecked &&
                Passing.selectedEmergencyMessageSetup.selectedContactList.isEmpty() &&
                Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isNotEmpty()
            ) {
                holder.itemView.switch2.isChecked = false
                Toast.makeText(
                    context,
                    "You must add a contact in order to activate",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (holder.itemView.switch2.isChecked &&
                Passing.selectedEmergencyMessageSetup.selectedContactList.isNotEmpty() &&
                Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isEmpty()
            ) {
                holder.itemView.switch2.isChecked = false
                Toast.makeText(
                    context,
                    "You must add a key phrase in order to activate",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (holder.itemView.switch2.isChecked &&
                Passing.selectedEmergencyMessageSetup.selectedContactList.isEmpty() &&
                Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isEmpty()
            ) {
                holder.itemView.switch2.isChecked = false
                Toast.makeText(
                    context,
                    "You must add a key phrase and a contact in order to activate",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                holder.itemView.activeSwitchTextView
                    .setTextColor(Color.parseColor("#B50909"))
                holder.itemView.activeSwitchTextView.text = "Inactive"
                Passing.selectedEmergencyMessageSetup.activeEMS = false
                Toast.makeText(
                    context,
                    "You have deactivated VRE service for EMS " + mutableList[position].title,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}