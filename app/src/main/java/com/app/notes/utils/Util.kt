package com.app.notes.utils

import android.app.AlertDialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.notes.R
import com.app.notes.ui.interfaces.AlertDialogPositiveClickListener

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


    fun hideKeyboard(context: AppCompatActivity?) {
        val view = context?.currentFocus
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }


    /*
    *
    * method to show alert alert dialog message
    *
    * */


    fun showAlertDialog(
        context: Context?,
        message: String?,
        positiveText: String?,
        listener: AlertDialogPositiveClickListener?
    ) {
        try {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context?.getString(R.string.text_alert))
            builder.setMessage(message)
            builder.setPositiveButton(
                positiveText
            ) { dialogInterface, _ ->
                listener?.onPositiveClick()
                dialogInterface.dismiss()
            }
            builder.setNegativeButton(
                context?.getString(
                    android.R.string.cancel
                )
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            val alertDialog = builder.create() as AlertDialog
            alertDialog.setCancelable(false)
            alertDialog.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}