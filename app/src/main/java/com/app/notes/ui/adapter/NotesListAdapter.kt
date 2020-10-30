package com.app.notes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.notes.R
import com.app.notes.data.room.entity.Note
import com.app.notes.ui.interfaces.RecyclerViewOnLongClickListener
import kotlinx.android.synthetic.main.row_notes.view.*

class NotesListAdapter(
    private val context: Context,
    private val recyclerViewOnLongClickListener: RecyclerViewOnLongClickListener
) : PagedListAdapter<Note, NotesListAdapter.ViewHolder>(DiffUtilCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_notes, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val note = getItem(position)
            holder.tvTitle.text = note?.title
            holder.tvDescription.text = note?.description
            if (note!!.isPinned) {
                holder.tvPinned.visibility = View.VISIBLE
            } else {
                holder.tvPinned.visibility = View.GONE
            }
            holder.itemView.setOnLongClickListener {
                recyclerViewOnLongClickListener.onLongClick(note)
                true
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tv_title
        val tvDescription: TextView = itemView.tv_description
        val tvPinned: TextView = itemView.tv_pin

    }
}