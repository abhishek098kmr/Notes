package com.app.notes.ui.interfaces

import androidx.fragment.app.Fragment

interface FragmentListener {
    fun addFragment(fragment: Fragment, isAddBackStack:Boolean,isUpdate: Boolean)
    fun replaceFragment(fragment: Fragment, isAddBackStack:Boolean)
    fun onNoteAdded()
}