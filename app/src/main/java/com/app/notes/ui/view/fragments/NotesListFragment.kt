package com.app.notes.ui.view.fragments


import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.notes.R
import com.app.notes.data.room.entity.Note
import com.app.notes.ui.adapter.NotesListAdapter
import com.app.notes.ui.interfaces.FragmentListener
import com.app.notes.ui.interfaces.RecyclerViewOnLongClickListener
import com.app.notes.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_notes_list.view.*
import kotlinx.android.synthetic.main.layout_note_item_dialog.view.*

/**
 * A simple [Fragment] subclass.
 */
class NotesListFragment : Fragment() {
    private var callBackListener: FragmentListener? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var rvNotes: RecyclerView
    private lateinit var mAdapter: NotesListAdapter
    private lateinit var notesList: ArrayList<Note>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity!!.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        }        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvNotes = view.rv_notes_list
        notesList = ArrayList()
        setAdapterToRecyclerView()
        getNotesList()
    }

    private fun setAdapterToRecyclerView() {
        rvNotes.layoutManager = LinearLayoutManager(activity!!)
        rvNotes.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
        mAdapter =
            NotesListAdapter(activity!!, notesList, object : RecyclerViewOnLongClickListener {
                override fun onLongClick(note: Note) {
                    showOpenDialogBox(note)
                }
            })
        rvNotes.adapter = mAdapter
    }

    private fun showOpenDialogBox(note: Note) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val view =
            LayoutInflater.from(activity!!).inflate(R.layout.layout_note_item_dialog, null, false)
        dialog.setContentView(view)
        view.tv_pin.setOnClickListener {
            note.isPinned = true
            viewModel.updateNote(note)
            dialog.cancel()
        }
        view.tv_update.setOnClickListener {
            callBackListener?.addFragment(AddNotesFragment.newInstance(note),
                isAddBackStack = true,
                isUpdate = true
            )
            dialog.cancel()
        }
        view.tv_delete.setOnClickListener {
            viewModel.deleteNote(note)
            dialog.cancel()
        }
        view.tv_cancel.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()

    }

    private fun getNotesList() {
        viewModel.getUserNotes().observe(this, Observer {
            if (it?.size!! > 0) {
                notesList.clear()
                notesList.addAll(it)
                mAdapter.notifyDataSetChanged()
                rvNotes.smoothScrollToPosition(0)
            }
        })
    }

    fun setCallbackListener(callBackListener: FragmentListener) {
        this.callBackListener = callBackListener
    }


}
