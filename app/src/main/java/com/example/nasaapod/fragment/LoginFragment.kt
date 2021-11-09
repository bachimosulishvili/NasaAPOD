package com.example.nasaapod.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nasaapod.R
import com.example.nasaapod.auth
import com.example.nasaapod.databinding.FragmentLoginBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider

var credential : AuthCredential? = null
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun next() = findNavController().navigate(R.id.action_loginFragment_to_APODFragment)

    private fun checkFragment(){
    }
    private fun init() {
        checkUser()
        buttonListeners()
        textViewListeners()
    }

    override fun onResume() {
        super.onResume()
        binding.etEmail.setText("")
        binding.etPassword.setText("")
        binding.etRepeatPassword.setText("")
    }

    private fun checkUser(){
        val currentUser = auth.currentUser
        if (currentUser != null ){
            findNavController().navigate(R.id.action_loginFragment_to_APODFragment)
        }
    }


    private fun buttonListeners() {
        val email = binding.etEmail
        val password = binding.etPassword
        val repeatPassword = binding.etRepeatPassword

        binding.btnLogIn.setOnClickListener {
            logIn(email, password)
        }
        binding.btnSignUp.setOnClickListener {
            signUp(email, password, repeatPassword)
        }


    }

    private fun textViewListeners() {
        binding.tvSignUpNow.setOnClickListener {
            hasNotAccount()
        }
        binding.tvLogIn.setOnClickListener {
            hasAccount()
        }
    }

    private fun hasNotAccount() {
        binding.apply {
            boxRepeat.visibility = View.VISIBLE
            btnLogIn.visibility = View.GONE
            tvDontHaveAccount.visibility = View.GONE
            tvSignUpNow.visibility = View.GONE
            btnSignUp.visibility = View.VISIBLE
            tvHaveAccount.visibility = View.VISIBLE
            tvLogIn.visibility = View.VISIBLE
        }
    }

    private fun hasAccount() {
        binding.apply {
            boxRepeat.visibility = View.GONE
            btnLogIn.visibility = View.VISIBLE
            tvDontHaveAccount.visibility = View.VISIBLE
            tvSignUpNow.visibility = View.VISIBLE
            btnSignUp.visibility = View.GONE
            tvHaveAccount.visibility = View.GONE
            tvLogIn.visibility = View.GONE
        }
    }

    private fun logIn(email: AppCompatEditText, password: AppCompatEditText) {
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        binding.boxEmail.boxBackgroundColor = Color.WHITE
        binding.boxPassword.boxBackgroundColor = Color.WHITE
        binding.tvError.visibility = View.INVISIBLE
        if (emailText.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(emailText)
                .matches()
        ) {
            if (passwordText.isNotEmpty() && passwordText.length >= 8) {
                auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "signInWithEmail:success")
                            credential = EmailAuthProvider.getCredential(emailText, passwordText)
                            next()
                        } else {
                            binding.tvError.apply {
                                visibility = View.VISIBLE
                                text = getString(R.string.error_incorrect)
                            }
                        }
                    }
            } else {
                binding.boxPassword.boxBackgroundColor = Color.parseColor("#FF9C9C")
                binding.tvError.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.error_password_check)
                }
            }
        } else {
            binding.boxEmail.boxBackgroundColor = Color.parseColor("#FF9C9C")
            binding.tvError.apply {
                visibility = View.VISIBLE
                text = getString(R.string.error_email)
            }

        }
    }


    private fun signUp(
        email: AppCompatEditText,
        password: AppCompatEditText,
        repeatPassword: AppCompatEditText,
    ) {
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        val repeatPasswordText = repeatPassword.text.toString()
        binding.boxEmail.boxBackgroundColor = Color.WHITE
        binding.boxPassword.boxBackgroundColor = Color.WHITE
        binding.boxRepeat.boxBackgroundColor = Color.WHITE
        if (emailText.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(emailText)
                .matches()
        ) {
            if (passwordText.isNotEmpty() && passwordText.length > 7) {
                if (repeatPasswordText.isNotEmpty() && repeatPasswordText == passwordText) {
                    auth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                Log.d("TAG", "createUserWithEmail:success")
                                credential = EmailAuthProvider.getCredential(emailText, passwordText)
                                next()
                            } else {
                                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                binding.tvError.apply {
                                    visibility = View.VISIBLE
                                    text = getString(R.string.error_exists)
                                }
                            }
                        }
                } else {
                    binding.boxPassword.boxBackgroundColor = Color.parseColor("#FF9C9C")
                    binding.boxRepeat.boxBackgroundColor = Color.parseColor("#FF9C9C")
                    binding.tvError.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.error_pw_match)
                    }
                }
            } else {
                binding.boxPassword.boxBackgroundColor = Color.parseColor("#FF9C9C")
                binding.tvError.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.error_pw_length)
                }
            }
        } else {
            binding.boxEmail.boxBackgroundColor = Color.parseColor("#FF9C9C")
            binding.tvError.apply {
                visibility = View.VISIBLE
                text = getString(R.string.error_email)
            }
        }

    }
}




