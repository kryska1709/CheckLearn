package com.example.checklearn.data

import com.example.checklearn.model.TestResult
import com.example.checklearn.view.TestScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    suspend fun getTestsHistory(): List<TestResult>{
        val userId = auth.currentUser?.uid ?: return emptyList()
        return firestore
            .collection("users")
            .document(userId)
            .collection("tests")
            .get().await().toObjects(TestResult::class.java)
    }
}