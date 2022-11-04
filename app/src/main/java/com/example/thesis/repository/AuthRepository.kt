package com.example.thesis.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.thesis.views.auth.ResponseStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val authDatabase: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    fun register(userName: String, email: String, password: String) = flow {
        emit(ResponseStatus.Loading)
        if (userName.isNotEmpty() &&email.isNotEmpty()&&password.length>5) {
            val user = authDatabase.createUserWithEmailAndPassword(email.trim(), password.trim()).await().user
            if (user != null) {
                updateProfile(userName)
                val emailAddress = user.email
                val uid = user.uid
                val receivedFriendRequests = ArrayList<String>()
                val friends = ArrayList<String>()
                val userData = hashMapOf("email" to emailAddress, "username" to userName, "uid" to uid, "receivedFriendRequests" to receivedFriendRequests, "friends" to friends)
                firestore.collection("users").document(userName).set(userData)
                    .addOnSuccessListener { Log.d("FB", "DocumentSnapshot written") }
                    .addOnFailureListener { e -> Log.d("FB", "Error adding document", e) }
                val share = ArrayList<String>()
                val shareData = hashMapOf("receivedShareRequests" to share)
                firestore.collection("shares").document(userName).set(shareData)
                    .addOnSuccessListener { Log.d("FB", "DocumentSnapshot written") }
                    .addOnFailureListener { e -> Log.d("FB", "Error adding document", e) }
            }
            emit(ResponseStatus.Success(""))
        }
        else
            emit(ResponseStatus.Failure(""))
    }.catch { error ->
        error.message?.let { errorMessage ->
            emit(ResponseStatus.Failure(errorMessage))
        }
    }

    private fun updateProfile(username: String) {
        // [START update_profile]
        val user = authDatabase.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = username
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }

    fun sendPasswordResetEmail(email: String) {
        authDatabase.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("email sent", "EMail sent")
            }
        }
    }

    fun signIn(email: String, password: String) = flow {
        emit(ResponseStatus.Loading)
        authDatabase.signInWithEmailAndPassword(email, password).await().user
        emit(ResponseStatus.Success(""))
    }.catch { error ->
        error.message?.let { errorMessage ->
            emit(ResponseStatus.Failure(errorMessage))
        }
    }

    fun signOut() {
        authDatabase.signOut()
    }

}