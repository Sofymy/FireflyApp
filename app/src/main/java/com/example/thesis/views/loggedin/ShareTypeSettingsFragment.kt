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
import com.example.thesis.databinding.FragmentShareSettingsBinding
import com.example.thesis.databinding.FragmentShareTargetSettingsBinding
import com.example.thesis.databinding.FragmentShareTypeSettingsBinding
import com.example.thesis.model.User
import com.example.thesis.viewModels.FriendsViewModel
import com.example.thesis.viewModels.LocationViewModel

class ShareTypeSettingsFragment : Fragment(){
    private lateinit var binding: FragmentShareTypeSettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_share_type_settings, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.type.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("type", ShareTargetType.MANUAL.toString())
            it.findNavController().navigate(R.id.action_shareTypeSettingsFragment_to_shareManualTargetFragment, bundle)
        }

        binding.selectOnMap.setOnClickListener{
            val user = arguments?.get("user").toString()
            val bundle = Bundle()
            bundle.putString("user", user)
            bundle.putString("type", ShareTargetType.MAP.toString())
            it.findNavController().navigate(R.id.action_shareTypeSettingsFragment_to_shareTargetSettingsFragment, bundle)
        }

    }
}