package com.example.nasaapod.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nasaapod.R
import com.example.nasaapod.auth
import com.example.nasaapod.databinding.FragmentProfileBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.etEmail.setText(auth.currentUser!!.email.toString())
        listeners()
    }

    private fun listeners() {

        binding.ivLogOut.setOnClickListener {
            auth.signOut()
            this.findNavController().popBackStack(R.id.loginFragment, false)

        }


        binding.llChangePassword.setOnClickListener {
            binding.boxEmail.visibility = View.GONE
            binding.llChangePassword.visibility = View.GONE
            binding.modePasswordChange.visibility = View.VISIBLE
        }

        binding.btnChange.setOnClickListener {

        }

        binding.btnCancel.setOnClickListener {
            binding.boxEmail.visibility = View.VISIBLE
            binding.llChangePassword.visibility = View.VISIBLE
            binding.modePasswordChange.visibility = View.GONE
        }

    }

    private fun changePassword(credential: AuthCredential, newPassword: String) {

        val oldPassword = binding.etOldPassword
        val newPassword = binding.etNewPassword
        val newPasswordRepeat = binding.etNewPasswordRepeat

        val oldPasswordText = oldPassword.text.toString()
        val newPasswordText = newPassword.text.toString()
        val newPasswordRepeatText = newPasswordRepeat.text.toString()

        var newCredential = com.example.nasaapod.fragment.credential
        try {
            newCredential =
                EmailAuthProvider.getCredential(auth.currentUser!!.email!!, oldPasswordText)
        } catch (e: Exception) {
            newCredential = com.example.nasaapod.fragment.credential
        }

        if (newPasswordText == newPasswordRepeatText) {
            auth.currentUser!!.reauthenticate(credential!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser!!.updatePassword(newPasswordText)
                } else {

                }
            }
        } else {

        }



    }
}