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

class LoginFragment : Fragment() {

    lateinit var mBinding: FragmentLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        mBinding.tvSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }

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
                    if (auth.currentUser!!.isEmailVerified == true) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                        Toast.makeText(requireContext(), "successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "email is not verified",
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

        if (auth.currentUser?.isEmailVerified == true || auth.currentUser != null) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }
    }
}

