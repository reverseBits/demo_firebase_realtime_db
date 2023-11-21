package com.example.mysicu.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment : Fragment() {

    lateinit var mBinding: FragmentLoginBinding
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    private var access: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Admin")
            .child(auth.currentUser?.uid.toString())

        mBinding.tvSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                access = snapshot.child("access").getValue(String::class.java)?.toUpperCase()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        mBinding.btnLogin.setOnClickListener {

            val email = mBinding.edtEmail.text.toString()
            val password = mBinding.edtPassword.text.toString()

            if (email.isEmpty()) {
                mBinding.edtEmail.error = "plz Enter Filed"
            } else if (password.isEmpty()) {
                mBinding.edtPassword.error = "plz Enter Filed"
            } else if (password.length <= 6) {
                mBinding.edtPassword.error = "plz Enter Max 6 Chr"
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
//                    if (auth.currentUser!!.isEmailVerified == true) {
//                    val access =
                    Log.d("TAG", "onViewCreated: $access")
                    if (access.equals("YES")) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                        Toast.makeText(requireContext(), "successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "You are not access",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "onCreate: ${it.message}")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser != null || access.equals("YES")) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        } else {
            Toast.makeText(
                requireContext(),
                "You are not access on",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

