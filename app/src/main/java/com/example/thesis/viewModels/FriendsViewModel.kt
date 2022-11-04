package com.example.thesis.viewModels

import com.example.thesis.repository.FriendsRepository
import androidx.lifecycle.ViewModel
import com.example.thesis.model.User
import com.example.thesis.views.loggedin.CurrentFriendsAdapter
import com.example.thesis.views.loggedin.FriendAdapter
import com.example.thesis.views.loggedin.FriendRequestAdapter
import com.example.thesis.views.loggedin.ShareFriendsAdapter

class FriendsViewModel : ViewModel(){
    private var friendsRepository = FriendsRepository()

    fun getAllUsers(data: ArrayList<User>, adapter: FriendAdapter){
        friendsRepository.getAllUsers(data, adapter)
    }

    fun sendFriendRequest(user: User) {
        friendsRepository.addFriendRequest(user)
    }


    fun getNewRequests(data: ArrayList<User>, adapter: FriendRequestAdapter){
        return friendsRepository.getNewRequests(data, adapter)
    }

    fun addFriend(user: User, data: ArrayList<User>) {
        friendsRepository.addFriend(user ,data)
    }

    fun loadAllRequest(data: ArrayList<User>, adapter: FriendRequestAdapter) {
        friendsRepository.loadAllRequests(data, adapter)
    }

    fun loadAllFriends(data: ArrayList<User>, adapter: CurrentFriendsAdapter) {
        friendsRepository.loadAllFriends(data, adapter)
    }

    fun loadAllFriendsToShare(data: ArrayList<User>, adapter: ShareFriendsAdapter) {
        friendsRepository.loadAllFriendsToShare(data, adapter)
    }

    fun getNewFriend(data: ArrayList<User>, adapter: CurrentFriendsAdapter){
        friendsRepository.getNewFriend(data, adapter)
    }


}