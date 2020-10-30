package com.app.notes.ui.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.notes.R
import com.app.notes.ui.interfaces.FragmentListener
import com.app.notes.ui.view.fragments.AddNotesFragment
import com.app.notes.ui.view.fragments.LoginFragment
import com.app.notes.ui.view.fragments.NotesListFragment
import com.app.notes.utils.Constants
import com.app.notes.utils.PreferenceUtil
import com.app.notes.utils.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (PreferenceUtil.getBoolean(Constants.PREF_IS_LOGGED_IN, false)) {
            addFragment(NotesListFragment(), isAddBackStack = false, isUpdate = false)
        } else {
            addFragment(LoginFragment(), isAddBackStack = false, isUpdate = false)
        }
        fab_add_note.setOnClickListener {
            addFragment(AddNotesFragment(), isAddBackStack = true, isUpdate = false)
        }
    }

    /*
  *
  * method to add fragment and check fragment will add in back stack or not
  *
  * */

    override fun addFragment(fragment: Fragment, isAddBackStack: Boolean, isUpdate: Boolean) {
        if (isAddBackStack) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .add(R.id.fl_container, fragment).addToBackStack(null).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .add(R.id.fl_container, fragment).commit()
        }
        checkForTitle(fragment, isUpdate)
    }

    /*
  *
  * method to replace fragment and check fragment will add in back stack or not
  *
  * */

    override fun replaceFragment(fragment: Fragment, isAddBackStack: Boolean) {
        if (isAddBackStack) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.fl_container, fragment).addToBackStack(null).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.fl_container, fragment).commit()
        }
        checkForTitle(fragment, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            Util.hideKeyboard(this)
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is LoginFragment -> fragment.setCallbackListener(this)

            is AddNotesFragment -> fragment.setCallbackListener(this)

            is NotesListFragment -> fragment.setCallbackListener(this)


        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fl_container)
        if (fragment is AddNotesFragment) {
            supportFragmentManager.popBackStackImmediate()
            checkForTitle(supportFragmentManager.findFragmentById(R.id.fl_container)!!, false)
        } else {
            finish()
        }
    }

    /*
    *
    * check for loaded fragment and change toolbar title accordingly
    *
    * */

    private fun checkForTitle(fragment: Fragment, isUpdate: Boolean) {
        when (fragment) {
            is LoginFragment -> {
                addTitleToToolbar(getString(R.string.text_sign_in), false)
                fab_add_note.visibility = View.GONE
            }
            is NotesListFragment -> {
                addTitleToToolbar(getString(R.string.text_notes), false)
                fab_add_note.visibility = View.VISIBLE
            }
            is AddNotesFragment -> {
                if (isUpdate) {
                    addTitleToToolbar(getString(R.string.text_update_note), true)
                } else {
                    addTitleToToolbar(getString(R.string.text_add_note), true)
                }
                fab_add_note.visibility = View.GONE
            }
        }
    }


    /*
   *
   * add title to toolbar
   *
   * */

    private fun addTitleToToolbar(title: String, isHomeButtonEnabled: Boolean) {
        toolbar.title = title
        toolbar.setNavigationIcon(R.drawable.ic_white_back)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(isHomeButtonEnabled)
        supportActionBar?.setHomeButtonEnabled(isHomeButtonEnabled)
    }

    override fun onNoteAdded() {
        onBackPressed()
    }

}
