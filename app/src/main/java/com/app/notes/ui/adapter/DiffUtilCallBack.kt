package com.app.notes.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.app.notes.data.room.entity.Note

class DiffUtilCallBack : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
        /*oldItem.title == newItem.title
                && oldItem.description == newItem.description
                && oldItem.isPinned==newItem.isPinned
                && oldItem.modified==newItem.modified*/

    }

}