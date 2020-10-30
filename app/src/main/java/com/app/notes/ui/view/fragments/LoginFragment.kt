package com.app.notes.ui.view.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.notes.R
import com.app.notes.data.room.entity.User
import com.app.notes.ui.interfaces.FragmentListener
import com.app.notes.ui.viewmodel.MainViewModel
import com.app.notes.utils.Constants
import com.app.notes.utils.PreferenceUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_login.view.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private var callBackListener: FragmentListener? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureGoogleSignInClient()
        view.btn_sign_in.setSize(SignInButton.SIZE_WIDE)
        view.btn_sign_in.setOnClickListener {
            signInWithGoogle()
        }
    }

    /*
    *
    * method to initialize google sign in client
    *
    * */


    private fun configureGoogleSignInClient() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!, googleSignInOptions)
    }


    /*
    *
    * method to check last sign in account details or sign in with new account
    *
    * */


    private fun signInWithGoogle() {
        // if last signin account details found then account will not null
        /* val account = GoogleSignIn.getLastSignedInAccount(activity!!)
         if (account != null) {
             validateUser(account.id)
         } else {
             // Sign In with new account
             val signInIntent: Intent = mGoogleSignInClient.signInIntent
             startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)
         }*/
        mGoogleSignInClient.signOut()
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    /*
    *
    * method to validate user details in database if not found then save user details to database
    *
    * */


    private fun validateUser(id: String?) {
        viewModel.getUser(userId = id!!).observe(viewLifecycleOwner, Observer {
            if (it == null) {
                val user = User(userId = id)
                viewModel.addUser(user)
            }
            Log.e("login_id",id)
            PreferenceUtil.putAny(Constants.PREF_USER_ID, id)
            PreferenceUtil.putAny(Constants.PREF_IS_LOGGED_IN, true)
            callBackListener?.replaceFragment(NotesListFragment(), false)
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    /*
    *
    * method to get account details from google sign in result
    *
    * */

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
            validateUser(account.id)
        } catch (ex: ApiException) {
            ex.printStackTrace()
        }


    }

    fun setCallbackListener(callBackListener: FragmentListener) {
        this.callBackListener = callBackListener
    }


}
