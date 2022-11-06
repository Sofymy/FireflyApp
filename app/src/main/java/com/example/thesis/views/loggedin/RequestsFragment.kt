package com.example.thesis.views.loggedin

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thesis.R
import com.example.thesis.databinding.FragmentRequestsBinding
import com.example.thesis.model.User
import com.example.thesis.viewModels.FriendsViewModel

class RequestsFragment : Fragment() {

    private lateinit var binding: FragmentRequestsBinding
    private lateinit var friendsViewModel: FriendsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_requests, container, false)
        friendsViewModel = ViewModelProvider(this)[FriendsViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerview = binding.requestsRecyclerview
        recyclerview.layoutManager = LinearLayoutManager(context)
        val data = ArrayList<User>()
        val adapter = FriendRequestAdapter(FriendRequestAdapter.OnClickListener{ item->
            friendsViewModel.addFriend(item ,data)
        },data)
        friendsViewModel.loadAllRequest(data, adapter)
        friendsViewModel.getNewRequests(data, adapter)
        recyclerview.adapter = adapter
    }

}