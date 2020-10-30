package com.app.notes.data.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "notes")
@Parcelize
class Note(
    var title: String,
    var description: String,
    var userId: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var isPinned: Boolean = false,
    var created: Long = System.currentTimeMillis(),
    var modified: Long = System.currentTimeMillis()
) : Parcelable