package com.example.thesis.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BadgeRepository {
    private val authDatabase: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

}