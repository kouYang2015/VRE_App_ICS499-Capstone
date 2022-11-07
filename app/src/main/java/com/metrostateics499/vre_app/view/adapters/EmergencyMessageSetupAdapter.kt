package com.metrostateics499.vre_app.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.EmergencyMessageSetup
import kotlinx.android.synthetic.main.row.view.*

class EmergencyMessageSetupAdapter(
    private val mutableList: MutableList<EmergencyMessageSetup>,
//    val emergencyMessageSetupList: EmergencyMessageSetupList,
    val context: Context
) : RecyclerView.Adapter<EmergencyMessageSetupAdapter.ViewHolder>() {

    var viewSelected: View? = null
    var titleSelectedString: String = ""
    var viewSelectedBoolean: Boolean = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(emergencyMessageSetup: EmergencyMessageSetup) {

            itemView.title.text = emergencyMessageSetup.title
            itemView.description.text = emergencyMessageSetup.keyPhrase.keyPhrase
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
            Passing.selectedEmergencyMessageSetup = null
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
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}