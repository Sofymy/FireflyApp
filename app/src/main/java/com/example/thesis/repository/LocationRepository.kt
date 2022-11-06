package com.example.thesis.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.thesis.model.User
import com.example.thesis.views.loggedin.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class LocationRepository {

    var location = MutableLiveData<com.example.thesis.model.Location?>()
    var targetLocation = MutableLiveData<String>()

    private val authDatabase: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun addNewShare(username: String){
        firestore.collection("shares").document(username).update("receivedShareRequests", FieldValue.arrayUnion(authDatabase.currentUser?.displayName))
    }

    fun addNewTarget(target: String){
        val lat:Double
        val long:Double
        val type = ShareTargetType.MAP.toString()
        val userData = hashMapOf(
            "target" to target,
            "type" to type
        )

        authDatabase.currentUser?.displayName?.let {
            firestore.collection("locations").document(it).set(userData)
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
                    if (d != null) {
                        val value = d.get("receivedShareRequests") as ArrayList<*>
                        for (document in value) {
                            val user = User(document.toString(), "")
                            if (user !in data) {
                                data.add(user)
                            }
                        }
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
                firestore.collection("shares").document(it1)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w(ContentValues.TAG, "Listen failed.", e)
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            val d = snapshot.data
                            val value = d?.get("receivedShareRequests") as ArrayList<*>
                            if (value.isNotEmpty() && value.last() != null) {
                                for(v in value) {
                                    val username = v.toString()
                                    val uid = ""
                                    val user = User(username, uid)
                                    if (user !in data) {
                                        data.add(user)
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
        authDatabase.currentUser?.displayName?.let {
            firestore.collection("locations").document(it).update("lat", lat,"long",long)
                .addOnSuccessListener { Log.d("FB", "DocumentSnapshot written") }
                .addOnFailureListener { e -> Log.d("FB", "Error adding document", e) }
        }
    }

    fun getLocation(username: String): Flow<com.example.thesis.model.Location> = flow {
        firestore.collection("locations").document(username).get().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val a = task.result
                if (a != null) {
                    val latitude = a["lat"] as? Double
                    val longitude = a["long"] as? Double
                    if(latitude!=null&&longitude!=null) {
                        Log.d("faillll", "inin")
                        location.value = com.example.thesis.model.Location(latitude, longitude, ShareLocationState.SHARING)
                    }
                    else{
                        location.value = com.example.thesis.model.Location(0.0, 0.0, ShareLocationState.STOPPED)
                    }
                }
            }
        }.await()
        location.value?.let { emit(it) }
    }

    fun getTarget(username: String): Flow<String> = flow {
        firestore.collection("locations").document(username).get().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val a = task.result
                if (a != null) {
                    val target = a["target"]
                    targetLocation.value = target as String?
                }
            }
        }.await()
        targetLocation.value?.let { emit(it) }
    }

    fun addNewManualTarget(target: String) {

        val type = ShareTargetType.MANUAL.toString()
        val userData = hashMapOf(
            "target" to target,
            "type" to type
        )

        authDatabase.currentUser?.displayName?.let {
            firestore.collection("locations").document(it).set(userData)
                .addOnSuccessListener { Log.d("FB", "DocumentSnapshot written") }
                .addOnFailureListener { e -> Log.d("FB", "Error adding document", e) }
        }
    }

    fun stopSharing(user: String) {
        authDatabase.currentUser?.let {
            it.displayName?.let { it1 ->
                firestore.collection("shares").document(user).update("receivedShareRequests", FieldValue.arrayRemove(it1))
                firestore.collection("users").document(user).update("follows", FieldValue.increment(1))
                firestore.collection("users").document(it1).update("shares", FieldValue.increment(1))
            }
            it.displayName?.let { it1 ->
                firestore.collection("locations").document(it1).delete()
                    .addOnSuccessListener { Log.d("FB", "DocumentSnapshot written") }
                    .addOnFailureListener { e -> Log.d("FB", "Error adding document", e) }

            }
        }
    }

}