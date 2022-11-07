package com.metrostateics499.vre_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row.view.*

class EditEmergencyMessageAdapter(
    private val mutableList: MutableList<EmergencyMessageSetup>,
//    private val emergencyMessageSetup: EmergencyMessageSetup,
//    val emergencyMessageSetupList: EmergencyMessageSetupList,
    val context: Context
) : RecyclerView.Adapter<EditEmergencyMessageAdapter.ViewHolder>() {

    var viewSelected: View? = null
    var titleSelectedString: String = ""
    var viewSelectedBoolean: Boolean = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(emergencyMessageSetup: EmergencyMessageSetup) {

            itemView.title.text = emergencyMessageSetup.title
        }
        fun bindItems2(emergencyMessageSetup: EmergencyMessageSetup) {

            itemView.description.text = emergencyMessageSetup.keyPhrase.keyPhrase
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindItems(emergencyMessageSetup)
        holder.bindItems(mutableList[position])
//        holder.bindItems2(mutableList[position])
        holder.itemView.setOnClickListener {
            viewSelected?.setBackgroundResource(
                androidx.appcompat.R.drawable
                    .abc_item_background_holo_light
            )
            viewSelectedBoolean = false
            titleSelectedString = ""
            Toast.makeText(
                context,
                "You have selected " + mutableList[position].title,
                Toast.LENGTH_SHORT
            ).show()
            viewSelected = holder.itemView
            titleSelectedString = mutableList[position].title
            viewSelectedBoolean = true

            holder.itemView.setBackgroundResource(
                androidx.appcompat.R.drawable.abc_list_pressed_holo_dark)
        }
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}