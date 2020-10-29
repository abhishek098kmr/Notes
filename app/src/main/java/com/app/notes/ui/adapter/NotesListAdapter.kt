package com.app.notes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.notes.R
import com.app.notes.data.room.entity.Note
import com.app.notes.ui.interfaces.RecyclerViewOnLongClickListener
import kotlinx.android.synthetic.main.row_notes.view.*

class NotesListAdapter(
    private val context: Context,
    private val notesList: ArrayList<Note>,
    private val recyclerViewOnLongClickListener: RecyclerViewOnLongClickListener
) : RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_notes, parent, false))


    override fun getItemCount(): Int = notesList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        holder.tvTitle.text = note.title
        holder.tvDescription.text = note.description
        holder.itemView.setOnLongClickListener {
            recyclerViewOnLongClickListener.onLongClick(note)
            true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tv_title
        val tvDescription: TextView = itemView.tv_description

    }
}