package com.example.thesis.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.thesis.repository.FriendsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thesis.model.User
import com.example.thesis.views.loggedin.CurrentFriendsAdapter
import com.example.thesis.views.loggedin.FriendAdapter
import com.example.thesis.views.loggedin.FriendRequestAdapter
import com.example.thesis.views.loggedin.ShareFriendsAdapter
import kotlinx.coroutines.launch

class FriendsViewModel : ViewModel(){
    private var friendsRepository = FriendsRepository()

    private val _requestNumber = MutableLiveData<Int>()
    val requestNumber: LiveData<Int> get() = _requestNumber


    fun getAllUsers(data: ArrayList<User>, adapter: FriendAdapter){
        friendsRepository.getAllUsers(data, adapter)
    }

    fun sendFriendRequest(user: User) {
        friendsRepository.addFriendRequest(user)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getNewRequests(data: ArrayList<User>, adapter: FriendRequestAdapter){
        viewModelScope.launch {
            friendsRepository.incrementRequestNumber().collect{ value->
                _requestNumber.value = value
            }
        }
        return friendsRepository.getNewRequests(data, adapter)
    }

    fun getRequestNumber() {
        viewModelScope.launch {
            friendsRepository.incrementRequestNumber().collect{ value->
                _requestNumber.value = value
            }
        }
    }

    fun addFriend(user: User, data: ArrayList<User>) {
        friendsRepository.addFriend(user ,data)
    }

    fun loadAllRequest(data: ArrayList<User>, adapter: FriendRequestAdapter) {
        friendsRepository.loadAllRequests(data, adapter)
        _requestNumber.postValue(friendsRepository.newRequests.value)
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