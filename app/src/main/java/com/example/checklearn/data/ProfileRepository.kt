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
    private val firestore = FirebaseFirestore.getInstance()//для бд
    private  val auth = FirebaseAuth.getInstance() //гет инстанс для получения единственный экземляр клааса
    //для того чтобы понять кто залогинен
    suspend fun saveUserProfile( //suspend функция для того чтобы не блокировать поток при выполнении (иначе приложение перегрузится и зависнет или вылетит)
        profile: UserProfile
    ){
        val userId = auth.currentUser?.uid  ?: return
        firestore.collection("users").document(userId).set(profile).await() //дождаться завершения запроса
    }

    fun getUserProfile(): Flow<UserProfile?> = callbackFlow{//поток для синхронного получения данных при изменениях. возвращает поток данных который автоматически обновляется при измениях
        val userId = auth.currentUser?.uid?: run {
            trySend(null)
            close()
            return@callbackFlow //колбэк держит связь с файрстор. если не получили получаем null
        }
        val listener = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot,error ->
                if (error!=null){
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(UserProfile::class.java))//отправляем новое значение в поток. toObject преобразуем поток в объект
            }
        awaitClose { listener.remove() }// если не отписаться то будет утечка памяти
    }
}