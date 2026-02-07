package com.example.checklearn.data

import com.example.checklearn.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository() {
    private val firestore = FirebaseFirestore.getInstance()
    private  val auth = FirebaseAuth.getInstance()

    suspend fun saveUserProfile(
        profile: UserProfile
    ){
        val userId = auth.currentUser?.uid  ?: return
        firestore.collection("users").document(userId).set(profile).await()
    }

    suspend fun getUserProfile(): UserProfile?{
        val userId = auth.currentUser?.uid ?: return null
        return firestore.collection("users").document(userId).get().await().toObject(UserProfile::class.java)
    }
}