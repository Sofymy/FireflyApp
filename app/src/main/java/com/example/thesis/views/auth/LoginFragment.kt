package com.example.thesis.views.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.thesis.R
import com.example.thesis.databinding.FragmentLoginBinding
import com.example.thesis.viewModels.LoginViewModel
import com.example.thesis.views.loggedin.ManagerActivity


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUp()
    }

    private fun signUp() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.authenticatedStateFlow.collect {
                when (it) {
                    is ResponseStatus.Init -> {}
                    is ResponseStatus.Loading -> {
                        Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
                    }
                    is ResponseStatus.Success -> {
                        Toast.makeText(context, "Successful sign in", Toast.LENGTH_LONG).show()
                        startActivity(Intent(activity, ManagerActivity::class.java))
                        activity?.finish()
                    }
                    is ResponseStatus.Failure -> {
                        Toast.makeText(context, "Failed sign in", Toast.LENGTH_LONG).show()
                    }

                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        binding.forgot.setOnClickListener { it.findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment) }
    }


}