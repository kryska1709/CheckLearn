package com.example.checklearn.data

import androidx.compose.runtime.snapshotFlow
import com.example.checklearn.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    fun getUserProfile(): Flow<UserProfile?> = callbackFlow{
        val userId = auth.currentUser?.uid?: run {
            trySend(null)
            close()
            return@callbackFlow
        }
        val listener = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot,error ->
                if (error!=null){
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(UserProfile::class.java))
            }
        awaitClose { listener.remove() }
    }
    fun signOut(){
        auth.signOut()
    }
}