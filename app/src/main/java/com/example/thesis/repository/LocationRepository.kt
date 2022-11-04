package com.example.thesis.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.thesis.model.User
import com.example.thesis.views.loggedin.CurrentFriendsAdapter
import com.example.thesis.views.loggedin.LocationCallback
import com.example.thesis.views.loggedin.ShareFriendsAdapter
import com.example.thesis.views.loggedin.ShareRequestsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class LocationRepository {

    var location = MutableLiveData<com.example.thesis.model.Location>()

    private val authDatabase: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun shareLocation(userNameSender: String, userNameReceiver: String, latitude: Double, longitude: Double) {
    }

    fun addNewShare(username: String){
        firestore.collection("shares").document(username).update("receivedShareRequests", FieldValue.arrayUnion(authDatabase.currentUser?.displayName))
    }

    fun addNewTarget(target: String){
        authDatabase.currentUser?.displayName?.let {
            firestore.collection("locations").document(it).set(target)
                .addOnSuccessListener { Log.d("FB", "DocumentSnapshot written") }
                .addOnFailureListener { e -> Log.d("FB", "Error adding document", e) }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun getNewShareRequest(data: ArrayList<User>, adapter: ShareFriendsAdapter) {
        authDatabase.currentUser?.let {
            it.displayName?.let { it1 ->
                firestore.collection("shares").document(it1)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w(ContentValues.TAG, "Listen failed.", e)
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            val d = snapshot.data
                            if(!d.isNullOrEmpty()) {
                                val value = d.get("receivedShareRequests") as ArrayList<*>
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

    @SuppressLint("NotifyDataSetChanged")
    fun getAllRequests(data: ArrayList<User>, adapter: ShareRequestsAdapter) {
        authDatabase.currentUser?.displayName?.let {
            firestore.collection("shares").document(it).get().addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val d: DocumentSnapshot? = task.result
                    val value = d?.get("receivedShareRequests") as ArrayList<*>
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
    fun getNewRequest(data: ArrayList<User>, adapter: ShareRequestsAdapter) {
        authDatabase.currentUser?.let {
            it.displayName?.let { it1 ->
                firestore.collection("receivedShareRequests").document(it1)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w(ContentValues.TAG, "Listen failed.", e)
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            val d = snapshot.data
                            if(!d.isNullOrEmpty()) {
                                val value = d.get("receivedShareRequests") as ArrayList<*>
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

    fun shareMyLocation(location: android.location.Location) {
        val lat = location.latitude
        val long = location.longitude
        val loc = hashMapOf("lat" to lat, "long" to long)
        authDatabase.currentUser?.displayName?.let {
            firestore.collection("locations").document(it).set(loc)
                .addOnSuccessListener { Log.d("FB", "DocumentSnapshot written") }
                .addOnFailureListener { e -> Log.d("FB", "Error adding document", e) }
        }
    }

    fun getLat(username: String): Flow<com.example.thesis.model.Location> = flow {
        firestore.collection("locations").document(username).get().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val a = task.result
                if (a != null) {
                    val latitude = a["lat"] as Double
                    val longitude = a["long"] as Double
                    location.value = com.example.thesis.model.Location(latitude, longitude)
                }
            }
        }.await()
        location.value?.let { emit(it) }
    }

}