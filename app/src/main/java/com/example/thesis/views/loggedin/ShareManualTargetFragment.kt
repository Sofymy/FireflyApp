package com.example.thesis.views.loggedin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thesis.R
import com.example.thesis.databinding.FragmentShareManualTargetBinding
import com.example.thesis.databinding.FragmentShareSettingsBinding
import com.example.thesis.model.User
import com.example.thesis.viewModels.FriendsViewModel
import com.example.thesis.viewModels.LocationViewModel

class ShareManualTargetFragment : Fragment(){

    private lateinit var binding: FragmentShareManualTargetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_share_manual_target, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener{
            val user = arguments?.get("user").toString()
            val bundle = Bundle()
            bundle.putString("user", user)
            bundle.putString("type", ShareTargetType.MANUAL.toString())
            bundle.putString("target", binding.editTextTextPersonName.text.toString())
            it.findNavController().navigate(R.id.action_shareManualTargetFragment_to_mapsFragment, bundle)
        }
    }
}

