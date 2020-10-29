package com.app.notes.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

object Util {

    /*
    *
    * method to show alert messages using toast
    * */

    fun showToast(context: Context, message: String?) {
        if (message?.isNotEmpty()!!) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }



    /*
   *
   * method to hide soft keyboard
   * */



    fun hideKeyboard(context: Context?) {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}