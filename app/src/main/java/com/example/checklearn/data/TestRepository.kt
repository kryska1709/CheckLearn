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
        val doc = firestore.collection("users")
            .document(userId)
            .collection("tests")
            .document()
        val resultWithId = result.copy(id = doc.id)
        doc.set(resultWithId).await()
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
                val results = snapshot?.documents?.map {
                    it.toObject(TestResult::class.java)!!.copy(id = it.id)
                } ?: emptyList()
                trySend(results)
            }
        awaitClose { listener.remove() }
    }

    suspend fun deleteTest(
        testId: String
    ){
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .collection("tests")
            .document(testId)
            .delete()
            .await()
    }

    suspend fun deleteAllTests(){
        val userId = auth.currentUser?.uid ?: return
        val testsCollection = firestore.collection("users")
            .document(userId)
            .collection("tests")
        val documents = testsCollection.get().await()
        documents.forEach {
            testsCollection.document(it.id).delete().await()
        }
    }
}