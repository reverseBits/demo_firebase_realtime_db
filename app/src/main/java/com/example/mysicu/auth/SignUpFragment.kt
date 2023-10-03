package com.example.mysicu.auth

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.mysicu.R
import com.example.mysicu.databinding.FragmentSignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignUpFragment : Fragment() {

    lateinit var mBinding: FragmentSignUpBinding
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()



        mBinding.tvLogin.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }

        mBinding.btnSignUp.setOnClickListener {

            val email = mBinding.edtEmail.text.toString()
            val password = mBinding.edtPassword.text.toString()
            val confirmPassword = mBinding.edtConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){

                if (password == confirmPassword){

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                        if (it.isSuccessful){
                            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                        }else{
                            Toast.makeText(context,it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(context,"Password not match", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context,"Empty fields are not allowed !!", Toast.LENGTH_SHORT).show()
            }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123 && resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)

            auth.signInWithCredential(credential).addOnCompleteListener {

                if (it.isSuccessful){
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
                }else{
                    Toast.makeText(context,"Login not successful",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context,"${it.message}",Toast.LENGTH_SHORT).show()
            }
        }
    }
}