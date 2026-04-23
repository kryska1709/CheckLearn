package com.example.checklearn.data

import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Suppress("DEPRECATION")
class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUser() = auth.currentUser

    fun signOut(){
        auth.signOut()
    }
}
@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if(idToken == null){
                onAuthError(ApiException(Status.RESULT_INTERNAL_ERROR))
                return@rememberLauncherForActivityResult
            }
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            scope.launch {
                try {
                    val authResult = Firebase.auth.signInWithCredential(credential).await()
                    onAuthComplete(authResult)
                } catch (e: Exception){
                    Log.e("AUTHREPOS", e.message.toString())
                    onAuthError(ApiException(Status.RESULT_INTERNAL_ERROR))
                }
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}