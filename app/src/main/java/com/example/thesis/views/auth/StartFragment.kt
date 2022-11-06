package com.example.thesis.views.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.thesis.R
import androidx.navigation.fragment.findNavController
import com.example.thesis.databinding.FragmentHomeBinding
import com.example.thesis.databinding.FragmentStartBinding


class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.login.setOnClickListener { Toast.makeText(context, "Szeretlek<3", Toast.LENGTH_LONG).show()
        it.findNavController().navigate(R.id.action_homeFragment_to_loginFragment) }
        binding.register.setOnClickListener { Toast.makeText(context, "Szeretlek<3", Toast.LENGTH_LONG).show()
            it.findNavController().navigate(R.id.action_homeFragment_to_registerFragment) }
    }

}