package com.example.thesis.views.loggedin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thesis.R
import com.example.thesis.databinding.FragmentSearchBinding
import com.example.thesis.model.User
import com.example.thesis.viewModels.FriendsViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var friendsViewModel: FriendsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        friendsViewModel = ViewModelProvider(this)[FriendsViewModel::class.java]
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
        val adapter = FriendAdapter(FriendAdapter.OnClickListener{ item->
            friendsViewModel.sendFriendRequest(item)
        },data)
        friendsViewModel.getAllUsers(data, adapter)
        recyclerview.adapter = adapter

    }

}