package com.app.notes.ui.interfaces

import com.app.notes.data.room.entity.Note

interface RecyclerViewOnLongClickListener {
    fun onLongClick(note: Note)
}