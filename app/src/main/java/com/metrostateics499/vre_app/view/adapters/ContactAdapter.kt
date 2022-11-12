package com.metrostateics499.vre_app.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.Contact
import kotlinx.android.synthetic.main.row.view.*

class ContactAdapter(
    private val mutableList: MutableList<Contact>,
    val context: Context
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var viewSelected: View? = null
    var titleSelectedString: String = ""
    var viewSelectedBoolean: Boolean = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(contact: Contact) {

            itemView.title.text = contact.name
            itemView.description.text = contact.phoneNumber
            if (Passing.selectedEmergencyMessageSetup?.findContactObject(contact) == true) {
                itemView.switch2.isChecked = true
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
            Toast.makeText(
                context,
                "You have selected " + mutableList[position].name,
                Toast.LENGTH_SHORT
            ).show()
            viewSelected = holder.itemView
            titleSelectedString = mutableList[position].name
            viewSelectedBoolean = true
            Passing.selectedContactObject = mutableList[position]

            holder.itemView.setBackgroundResource(
                androidx.appcompat.R.drawable.abc_list_pressed_holo_dark
            )
        }
        holder.itemView.switch2.setOnClickListener() {
            if (holder.itemView.switch2.isChecked) {
                Passing.selectedEmergencyMessageSetup?.addContact(mutableList[position])
                Toast.makeText(
                    context,
                    "You have added " + mutableList[position].name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Passing.selectedEmergencyMessageSetup?.removeContact(mutableList[position])
                Toast.makeText(
                    context,
                    "You have removed " + mutableList[position].name,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}