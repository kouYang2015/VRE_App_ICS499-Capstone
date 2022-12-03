package com.metrostateics499.vre_app.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.CallMessage
import kotlinx.android.synthetic.main.row.view.*
import kotlinx.android.synthetic.main.row.view.switch2
import kotlinx.android.synthetic.main.row_custom_text.view.*

class CallMessageAdapter(
    private val mutableList: MutableList<CallMessage>,
    val context: Context
) : RecyclerView.Adapter<CallMessageAdapter.ViewHolder>() {

    var viewSelected: View? = null
    var titleSelectedString: String = ""
    var viewSelectedBoolean: Boolean = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(callMessage: CallMessage) {
            itemView.title.text = callMessage.title
            itemView.description.text = callMessage.callMessage
            if (Passing.checkInitializationSelectedEmergencyMessageSetup()) {
                val checkedCallMessage =
                    Passing.selectedEmergencyMessageSetup.selectedCallMessages
                        .find { it == callMessage }
                if (checkedCallMessage != null) {
                    itemView.switch2.isChecked = true
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
                "You have selected " + mutableList[position].title +
                    ": " + mutableList[position].callMessage,
                Toast.LENGTH_SHORT
            ).show()
            viewSelected = holder.itemView
            titleSelectedString = mutableList[position].callMessage
            viewSelectedBoolean = true
            Passing.selectedCallMessageObject = mutableList[position]

            holder.itemView.setBackgroundResource(
                androidx.appcompat.R.drawable.abc_list_pressed_holo_dark
            )
        }
        if (Passing.checkInitializationSelectedEmergencyMessageSetup()) {
            holder.itemView.switch2.setOnClickListener {
                if (holder.itemView.switch2.isChecked) {
                    Passing.selectedEmergencyMessageSetup.selectedCallMessages
                        .add(mutableList[position])
                    Toast.makeText(
                        context,
                        "You have added " + mutableList[position].title +
                            ": " + mutableList[position].callMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Passing.selectedEmergencyMessageSetup.selectedCallMessages
                        .remove(mutableList[position])
                    Toast.makeText(
                        context,
                        "You have removed " + mutableList[position].callMessage,
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