package com.example.checklearn.network

import android.content.Context
import com.example.checklearn.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

@Suppress("DEPRECATION")
class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    fun getGoogleSignInClient(
        context: Context
        ): GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient
    }

    suspend fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount
    ): Result<Unit>{
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    fun getCurrentUser() = auth.currentUser

    fun signOut(){
        auth.signOut()
        googleSignInClient.signOut()
    }
}