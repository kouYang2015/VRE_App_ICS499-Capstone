package com.metrostateics499.vre_app.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.databinding.RowBinding
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.Contact
import kotlinx.android.synthetic.main.row.view.*

class ContactAdapter(
    private val mutableList: MutableList<Contact>
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var viewSelected: View? = null
    var titleSelectedString: String = ""
    var viewSelectedBoolean: Boolean = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: RowBinding = RowBinding.bind(itemView)
        fun bind(contact: Contact) {
            binding.contactName.text = contact.name
            binding.contactPhone.text = contact.phoneNumber
            if (Passing.selectedEmergencyMessageSetup?.findContactObject(contact) == true) {
                itemView.switch2.isChecked = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mutableList[position])

        holder.itemView.setOnClickListener {
            highlightSelectedContact(holder, position)
        }
        holder.itemView.switch2.setOnClickListener() {
            if (holder.itemView.switch2.isChecked) {
                Passing.selectedEmergencyMessageSetup?.addContact(mutableList[position])
                Toast.makeText(
                    holder.itemView.context,
                    "You have added " + mutableList[position].name,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Passing.selectedEmergencyMessageSetup?.removeContact(mutableList[position])
                Toast.makeText(
                    holder.itemView.context,
                    "You have removed " + mutableList[position].name,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun highlightSelectedContact(holder: ViewHolder, position: Int) {
        viewSelected?.let {
            it.setBackgroundResource(
                androidx.appcompat.R.drawable
                    .abc_item_background_holo_light
            )
            viewSelectedBoolean = false
            Passing.selectedContact = null
        }
        Toast.makeText(
            holder.itemView.context,
            "You have selected " + mutableList[position].name,
            Toast.LENGTH_SHORT
        ).show()
        viewSelected = holder.itemView
        titleSelectedString = mutableList[position].name
        viewSelectedBoolean = true
        Passing.selectedContact = mutableList[position]

        holder.itemView.setBackgroundResource(
            androidx.appcompat.R.drawable.abc_list_pressed_holo_dark
        )
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}