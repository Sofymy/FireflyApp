package com.example.thesis.views.loggedin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thesis.R
import com.example.thesis.databinding.FragmentShareSettingsBinding
import com.example.thesis.model.User
import com.example.thesis.viewModels.FriendsViewModel
import com.example.thesis.viewModels.LocationViewModel

class ShareSettingsFragment : Fragment(){

    private lateinit var binding: FragmentShareSettingsBinding
    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var locationViewModel: LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_share_settings, container, false)
        friendsViewModel = ViewModelProvider(this)[FriendsViewModel::class.java]
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        binding.viewModelF = friendsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerview = binding.recyclerview
        recyclerview.layoutManager = LinearLayoutManager(context)
        val data = ArrayList<User>()
        val adapter = ShareFriendsAdapter(ShareFriendsAdapter.OnClickListener{ item ->
            val bundle = Bundle()
            bundle.putString("user", item.username)
            view.findNavController().navigate(R.id.action_shareSettingsFragment_to_shareTargetSettingsFragment, bundle)
        },data)
        friendsViewModel.loadAllFriendsToShare(data, adapter)
        recyclerview.adapter = adapter
    }
}
