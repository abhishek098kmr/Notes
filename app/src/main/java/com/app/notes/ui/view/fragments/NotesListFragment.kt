package com.app.notes.ui.view.fragments


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.notes.R
import com.app.notes.data.room.entity.Note
import com.app.notes.ui.adapter.NotesListAdapter
import com.app.notes.ui.interfaces.AlertDialogPositiveClickListener
import com.app.notes.ui.interfaces.FragmentListener
import com.app.notes.ui.interfaces.RecyclerViewOnLongClickListener
import com.app.notes.ui.viewmodel.MainViewModel
import com.app.notes.utils.Constants
import com.app.notes.utils.PreferenceUtil
import com.app.notes.utils.Util
import kotlinx.android.synthetic.main.fragment_notes_list.view.*
import kotlinx.android.synthetic.main.layout_note_item_dialog.view.*


/**
 * A simple [Fragment] subclass.
 */
class NotesListFragment : Fragment() {
    private var callBackListener: FragmentListener? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var rvNotes: RecyclerView
    private lateinit var tvNoNotes: TextView
    private lateinit var mAdapter: NotesListAdapter
    private lateinit var notesList: ArrayList<Note>
    private var listSize=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        tvNoNotes = view.tv_no_notes
        notesList = ArrayList()
        setAdapterToRecyclerView()
        viewModel.initializeNotes()
        observeNotesList()
        getNotes("")
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notes_list, menu)
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.text_search_your_notes)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getNotes(newText)
                return true
            }
        })
        searchView.setOnCloseListener {
            getNotes("")
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                logout()
                activity!!.viewModelStore.clear()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    /*
  *
  * method to show logout alert dialog and logout user after logout button
  *
  * */


    private fun logout() {
        Util.showAlertDialog(
            activity!!,
            getString(R.string.alert_message_logout),
            getString(R.string.menu_logout),
            object : AlertDialogPositiveClickListener {
                override fun onPositiveClick() {
                    PreferenceUtil.clear()
                    callBackListener?.replaceFragment(LoginFragment(), false)
                }
            })
    }

    /*
    *
    * method to get notes
    *
    * */

    private fun getNotes(userInput: String?) {
        viewModel.getUserNotes.value = userInput
    }

    /*
    *
    * initialize adapter with empty list
    * */

    private fun setAdapterToRecyclerView() {
        rvNotes.layoutManager = LinearLayoutManager(activity!!)
        rvNotes.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
        mAdapter =
            NotesListAdapter(activity!!, object : RecyclerViewOnLongClickListener {
                override fun onLongClick(note: Note) {
                    showOptionDialogBox(note)
                }
            })
        rvNotes.adapter = mAdapter
    }


    /*
    *
    *
    * method to show option dialog box to delete , edit and pin particular note
    *
    * */

    private fun showOptionDialogBox(note: Note) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val view =
            LayoutInflater.from(activity!!).inflate(R.layout.layout_note_item_dialog, null, false)
        if (note.isPinned) {
            view.tv_pin.text = getString(R.string.text_un_pin)
        } else {
            view.tv_pin.text = getString(R.string.text_pin)
        }
        dialog.setContentView(view)
        view.tv_pin.setOnClickListener {
            note.isPinned = !note.isPinned
            viewModel.updateNote(note)
            dialog.cancel()
        }
        view.tv_update.setOnClickListener {
            callBackListener?.addFragment(
                AddNotesFragment.newInstance(note),
                isAddBackStack = true,
                isUpdate = true
            )
            dialog.cancel()
        }
        view.tv_delete.setOnClickListener {
            deleteNote(note)
            dialog.cancel()
        }
        view.tv_cancel.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()

    }

    /*
    *
    * method to show delete alert dialog and delete note after delete button
    *
    * */

    private fun deleteNote(note: Note) {
        Util.showAlertDialog(
            activity!!,
            getString(R.string.alert_message_note_delete),
            getString(R.string.text_delete),
            object : AlertDialogPositiveClickListener {
                override fun onPositiveClick() {
                    viewModel.deleteNote(note)

                }
            })
    }


    /*
    *
    * method to observe  notes changes in local database
    *
    *
    * */

    private fun observeNotesList() {
        viewModel.userNotes.observe(viewLifecycleOwner, Observer {
            setDataToAdapter(it)
        })
    }


    /*
    *
    *
    * method to set list to recyclerview adapter and also check for emoty view
    *
    * */

    private fun setDataToAdapter(it: PagedList<Note>?) {
        mAdapter.submitList(it)
        if (it?.size!! > 0) {
            tvNoNotes.visibility = View.GONE
            if (listSize<= it.size) {
                Handler().postDelayed({
                    rvNotes.scrollToPosition(0)

                },Constants.SCROOL_DELAY)
            }
        } else {
            tvNoNotes.visibility = View.VISIBLE
        }
        listSize= it.size
    }


    /*
    *
    *
    * method to filter pinned and unpinned notes
    *
    * */


    private fun filterPinnedList(it: List<Note>): ArrayList<Note>? {
        val unPinlist = ArrayList<Note>()
        val notesList = ArrayList<Note>()

        for (note in it) {
            if (note.isPinned) {
                notesList.add(note)
            } else {
                unPinlist.add(note)
            }
        }
        notesList.addAll(unPinlist)
        return notesList
    }

    fun setCallbackListener(callBackListener: FragmentListener) {
        this.callBackListener = callBackListener
    }


}
