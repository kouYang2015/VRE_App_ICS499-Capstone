package com.metrostateics499.vre_app.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.KeyPhrase
import kotlinx.android.synthetic.main.row.view.*

class KeyPhraseAdapter(
    private val mutableList: MutableList<KeyPhrase>,
    val context: Context
) : RecyclerView.Adapter<KeyPhraseAdapter.ViewHolder>() {

    var viewSelected: View? = null
    var titleSelectedString: String = ""
    var viewSelectedBoolean: Boolean = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(keyPhrase: KeyPhrase) {
            itemView.title.text = keyPhrase.phrase
//            itemView.description.text = contact.phoneNumber
            if (Passing.checkInitializationSelectedEmergencyMessageSetup()) {
                val checkedKeyPhrase =
                    Passing.selectedEmergencyMessageSetup.
                        selectedKeyPhraseList.find { it == keyPhrase }
                if (checkedKeyPhrase != null) {
                    itemView.switch2.isChecked = true
                    itemView.description.text = "In use by this EM"
                } else if (keyPhrase.inUse) {
                    itemView.description.text = "In use by other EM"
                } else {
                    itemView.description.text = "Not in use"
                }
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
                "You have selected " + mutableList[position].phrase,
                Toast.LENGTH_SHORT
            ).show()
            viewSelected = holder.itemView
            titleSelectedString = mutableList[position].phrase
            viewSelectedBoolean = true
            Passing.selectedKeyPhraseObject = mutableList[position]

            holder.itemView.setBackgroundResource(
                androidx.appcompat.R.drawable.abc_list_pressed_holo_dark
            )
        }
        if (Passing.checkInitializationSelectedEmergencyMessageSetup()) {
            holder.itemView.switch2.setOnClickListener {
                if (holder.itemView.switch2.isChecked && !mutableList[position].inUse) {
                    Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.
                        add(mutableList[position])
                    mutableList[position].inUse = true
                    holder.itemView.description.text = "In use by this EM"
                    Toast.makeText(
                        context,
                        "Added Key Phrase: " + mutableList[position].phrase,
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (holder.itemView.switch2.isChecked && mutableList[position].inUse) {
                    holder.itemView.switch2.isChecked = false
                    Toast.makeText(
                        context,
                        "That Key Phrase is already in use by another Emergency Message",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.
                        remove(mutableList[position])
                    mutableList[position].inUse = false
                    holder.itemView.description.text = "Not in use"
                    Toast.makeText(
                        context,
                        "Removed Key Phrase: " + mutableList[position].phrase,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}