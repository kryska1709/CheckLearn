package com.example.checklearn.data

import com.example.checklearn.model.TestResult
import com.example.checklearn.model.UserProfile
import com.example.checklearn.view.TestScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TestRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun saveTest(
        result: TestResult
    ){
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .collection("tests")
            .add(result).await()
    }
    fun getTestHistory(): Flow<List<TestResult>> = callbackFlow{
        val userId = auth.currentUser?.uid?: run {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val listener = firestore.collection("users").document(userId).collection("tests")
            .addSnapshotListener { snapshot,error ->
                if (error!=null){
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObjects(TestResult::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }
}