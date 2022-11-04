package com.example.thesis.views.loggedin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thesis.R
import com.example.thesis.databinding.FragmentCurrentFriendsBinding
import com.example.thesis.model.User
import com.example.thesis.viewModels.FriendsViewModel
import java.util.ArrayList

class CurrentFriendsFragment : Fragment() {

    private lateinit var binding: FragmentCurrentFriendsBinding
    private lateinit var friendsViewModel: FriendsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_current_friends, container, false)
        friendsViewModel = ViewModelProvider(this)[FriendsViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerview = binding.currentFriendsRecyclerview
        recyclerview.layoutManager = LinearLayoutManager(context)
        val data = ArrayList<User>()
        val adapter = CurrentFriendsAdapter(data)
        friendsViewModel.loadAllFriends(data, adapter)
        friendsViewModel.getNewFriend(data,adapter)
        recyclerview.adapter = adapter
    }

}
