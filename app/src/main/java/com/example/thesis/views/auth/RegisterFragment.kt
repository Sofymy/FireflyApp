package com.example.thesis.views.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.thesis.R
import com.example.thesis.databinding.FragmentRegisterBinding
import com.example.thesis.viewModels.LoginViewModel

class RegisterFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register()
    }

    private fun register() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.registerUserStateFlow.collect {
                when (it) {
                    is ResponseStatus.Init -> {}
                    is ResponseStatus.Loading -> {
                        Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
                    }
                    is ResponseStatus.Success -> {
                        Toast.makeText(context, "Successful registration", Toast.LENGTH_LONG).show()
                        view?.findNavController()?.navigate(R.id.action_registerFragment2_to_loginFragment)
                    }
                    is ResponseStatus.Failure -> {
                        Toast.makeText(context, "Failed registration", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}


