package com.example.thesis.repository

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.thesis.MyFirebaseMessagingService
import com.example.thesis.model.User
import com.example.thesis.views.loggedin.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class FriendsRepository {
    private val requestFrom = MutableLiveData<String>()
    val newRequests = MutableLiveData(0)

    fun incrementRequestNumber() : Flow<Int?> {
        return flow{
            newRequests.postValue(newRequests.value?.plus(1))
            emit(newRequests.value)
        }.flowOn(Dispatchers.IO)
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val authDatabase: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun getAllUsers(data: ArrayList<User>, adapter: FriendAdapter) {
        firestore.collection("users").get().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val list: QuerySnapshot? = task.result
                if (list != null) {
                    for (document in list) {
                        val value = document.data
                        val username = value["username"]
                        val uid = value["uid"]
                        val user = User(username.toString(),uid.toString())
                        if(uid.toString()!= authDatabase.currentUser?.uid){
                            data.add(user)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadAllRequests(data: ArrayList<User>, adapter: FriendRequestAdapter) {
        authDatabase.currentUser?.displayName?.let {
            firestore.collection("users").document(it).get().addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val d: DocumentSnapshot? = task.result
                    if (d != null) {
                        val value = d.get("receivedFriendRequests") as ArrayList<*>
                        for (document in value) {
                            val user = User(document.toString(), "")
                            if (user !in data) {
                                data.add(user)
                                incrementRequestNumber()
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    fun getNewRequests(data: ArrayList<User>, adapter: FriendRequestAdapter) {
        authDatabase.currentUser?.let {
                it.displayName?.let { it1 ->
                    firestore.collection("users").document(it1)
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e)
                                return@addSnapshotListener
                            }
                            if (snapshot != null && snapshot.exists()) {
                                val d = snapshot.data
                                val value = d?.get("receivedFriendRequests") as ArrayList<*>
                                if (value.isNotEmpty() && value.last() != null) {
                                    for(v in value) {
                                        val username = v.toString()
                                        val uid = ""
                                        val user = User(username, uid)
                                        if (user !in data && requestFrom.value!=user.toString()) {
                                            data.add(user)
                                            incrementRequestNumber()
                                        }
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged()
                        }
                }
            }

    }


    fun addFriendRequest(user: User) {
        try {
            firestore.collection("users").document(user.username).update("receivedFriendRequests", FieldValue.arrayUnion(authDatabase.currentUser?.displayName))

        }catch (e: Exception) {
            Log.d("error", e.message.toString())
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addFriend(user: User, data: ArrayList<User>) {
        authDatabase.currentUser?.let {
            it.displayName?.let {
                    it1 -> firestore.collection("users").document(it1).update("friends", FieldValue.arrayUnion(user.username))
            }
            it.displayName?.let { it1 ->
                firestore.collection("users").document(it1).update("receivedFriendRequests", FieldValue.arrayRemove(user.username))
                data.remove(user)
                requestFrom.value = user.toString()
            }
        }
        firestore.collection("users").document(user.username).update("friends", FieldValue.arrayUnion(
            authDatabase.currentUser?.displayName
        ))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadAllFriends(data: ArrayList<User>, adapter: CurrentFriendsAdapter) {
        authDatabase.currentUser?.displayName?.let {
            firestore.collection("users").document(it).get().addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val d: DocumentSnapshot? = task.result
                    val value = d?.get("friends") as ArrayList<*>
                    for (document in value) {
                        val user = User(document.toString(),"")
                        if(user!in data) data.add(user)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadAllFriendsToShare(data: ArrayList<User>, adapter: ShareFriendsAdapter) {
        authDatabase.currentUser?.displayName?.let {
            firestore.collection("users").document(it).get().addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val d: DocumentSnapshot? = task.result
                    val value = d?.get("friends") as ArrayList<*>
                    for (document in value) {
                        val user = User(document.toString(),"")
                        if(user!in data) data.add(user)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getNewFriend(data: ArrayList<User>, adapter: CurrentFriendsAdapter) {
        authDatabase.currentUser?.let {
            it.displayName?.let { it1 ->
                firestore.collection("users").document(it1)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e)
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            val d = snapshot.data
                            if(!d.isNullOrEmpty()) {
                                val value = d["friends"] as ArrayList<*>
                                if (value.isNotEmpty() && value.last() != null) {
                                    for (v in value) {
                                        val username = v.toString()
                                        val uid = ""
                                        val user = User(username, uid)
                                        if (user !in data) data.add(user)
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
            }
        }
    }

}

