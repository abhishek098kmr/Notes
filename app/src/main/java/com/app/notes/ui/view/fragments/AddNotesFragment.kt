package com.app.notes.ui.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.app.notes.R
import com.app.notes.data.room.entity.Note
import com.app.notes.ui.interfaces.FragmentListener
import com.app.notes.ui.viewmodel.MainViewModel
import com.app.notes.utils.Constants
import com.app.notes.utils.PreferenceUtil
import com.app.notes.utils.Util
import kotlinx.android.synthetic.main.fragment_add_notes.*
import kotlinx.android.synthetic.main.fragment_add_notes.view.*

/**
 * A simple [Fragment] subclass.
 */
class AddNotesFragment : Fragment() {
    private var callBackListener: FragmentListener? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnAddNote:Button
    private var note = null as Note?

    /*
    *
    * get fragment instance and set arguments
    *
    * */

    companion object {
        fun newInstance(note: Note): AddNotesFragment {
            val bundle = Bundle()
            bundle.putParcelable(Constants.KEY_NOTE, note)
            val fragment = AddNotesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity!!.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_notes, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        note = arguments?.getParcelable(Constants.KEY_NOTE) as? Note
        etTitle = view.et_title
        etDescription = view.et_description
        btnAddNote=view.btn_add_note
        note?.let {
            setDataToUI()
        }
        btn_add_note.setOnClickListener {
            if (isValidated()) {
                Util.hideKeyboard(activity!!)
                addNoteInDatabase()
            }
        }

    }

    private fun setDataToUI() {
        etTitle.setText(note?.title)
        etDescription.setText(note?.description)
        btnAddNote.text=getString(R.string.text_update_note)


    }


    /*
    *
    * method to validate user inputs
    *
    * */

    private fun isValidated(): Boolean {
        when {
            etTitle.text.isEmpty() -> {
                Util.showToast(
                    activity!!,
                    getString(R.string.error_empty_title)
                )
                return false
            }
            etDescription.text.isEmpty() -> {
                Util.showToast(
                    activity!!,
                    getString(R.string.error_empty_description)
                )
                return false
            }

        }
        return true

    }

    /*
    *
    * method to add note in local database
    *
    * */


    private fun addNoteInDatabase() {
        if (note != null) {
            note?.title = etTitle.text.toString()
            note?.description = etDescription.text.toString()
            note?.modified = System.currentTimeMillis()
            viewModel.updateNote(note!!)
            Util.showToast(activity!!, getString(R.string.text_note_updated))
        } else {
            val note = Note(
                etTitle.text.toString(),
                etDescription.text.toString(),
                PreferenceUtil.getString(Constants.PREF_USER_ID, "")
            )
            viewModel.addNote(note)
            Util.showToast(activity!!, getString(R.string.text_note_added))
        }
        callBackListener?.onNoteAdded()
    }


    fun setCallbackListener(callBackListener: FragmentListener) {
        this.callBackListener = callBackListener
    }

}
