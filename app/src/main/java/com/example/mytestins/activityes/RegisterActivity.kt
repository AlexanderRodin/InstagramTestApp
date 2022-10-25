package com.example.mytestins.activityes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mytestins.R
import com.example.mytestins.activityes.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_namepass.*

class RegisterActivity : AppCompatActivity(), EmailFragment.Listener, NamePass.Listener {
    private var mEmail: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDataBase: DatabaseReference
    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mDataBase = FirebaseDatabase.getInstance().reference

        if (savedInstanceState == null) supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout, EmailFragment()).commit()

    }

    override fun onNext(email: String) {
        mEmail = email
        if (email.isNotEmpty()) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, NamePass())
                .addToBackStack(null)
                .commit()
        } else showToast("Pleas enter email")
    }

    override fun onRegister(fullName: String, password: String) {
        if (fullName.isNotEmpty() && password.isNotEmpty()) {
            val email = mEmail
            if (email != null) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            val user = mkUser(fullName, email)
                            val reference = mDataBase.child("users").child(it.result.user!!.uid)
                            reference.setValue(user).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    StartHomeActivity()
                                } else unkownRegisterError(it)

                            }
                        } else unkownRegisterError(it)

                    }
            } else {
                Log.e(TAG, "onRegister: email is null ")
                showToast("Pleas enter email")
                supportFragmentManager.popBackStack()
            }
        } else showToast("Please enter Full Name end Password")
    }

    private fun unkownRegisterError(it: Task<out Any>) {
        Log.e(TAG, " Failed to create user profile", it.exception)
        showToast("Something wrong happened, Please try again letter")
    }

    private fun StartHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun mkUser(fullName: String, email: String): User {
        val username = mkUsername(fullName)
        return User(name = fullName, username = username, email = email)
    }

    private fun mkUsername(fullName: String) =
        fullName.toLowerCase().replace("", ".")

}

//TODO Email, next button

class EmailFragment : androidx.fragment.app.Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onNext(email: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_register_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        next_btn.setOnClickListener {
            val email = email_input.text.toString()
            mListener.onNext(email)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
}

//TODO Full name, register, button

class NamePass : Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onRegister(fullName: String, password: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_register_namepass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        register_btn.setOnClickListener {
            val fullName = register_full_name_input.text.toString()
            val password = register_password_input.text.toString()
            mListener.onRegister(fullName, password)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
}