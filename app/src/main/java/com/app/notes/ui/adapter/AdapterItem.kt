package com.app.notes.ui.adapter

import com.app.notes.data.room.entity.Note

sealed class AdapterItem {
    abstract val id: Int

    data class NotesItem(val note: Note, override val id: Int = note.id) : AdapterItem()
    data class HeaderItem(val headerId: Int, override val id: Int = headerId) : AdapterItem()
}