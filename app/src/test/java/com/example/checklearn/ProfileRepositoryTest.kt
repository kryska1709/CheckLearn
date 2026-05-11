package com.example.checklearn

import com.example.checklearn.data.ProfileRepository
import com.example.checklearn.model.UserProfile
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileRepositoryTest {

    @MockK lateinit var mockFirestore: FirebaseFirestore
    @MockK lateinit var mockAuth: FirebaseAuth
    @MockK lateinit var mockUser: FirebaseUser
    @MockK lateinit var mockCollectionRef: CollectionReference
    @MockK lateinit var mockDocumentRef: DocumentReference
    @MockK lateinit var mockSetTask: Task<Void>
    @MockK lateinit var mockSnapshot: DocumentSnapshot

    private lateinit var repository: ProfileRepository
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(FirebaseFirestore::class, FirebaseAuth::class)
        every { FirebaseFirestore.getInstance() } returns mockFirestore
        every { FirebaseAuth.getInstance() } returns mockAuth
        repository = ProfileRepository()
    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun `saveUserProfile сохраняет профиль для авторизованного пользователя`() = runTest(dispatcher) {

        val profile = UserProfile(fullName = "Динар", classRoom = "22ит17")
        val userId = "uid_123"

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns userId
        every { mockFirestore.collection("users") } returns mockCollectionRef
        every { mockCollectionRef.document(userId) } returns mockDocumentRef
        every { mockDocumentRef.set(profile) } returns mockSetTask
        coEvery { mockSetTask.await() } returns mockk()

        repository.saveUserProfile(profile)

        coVerify(exactly = 1) { mockDocumentRef.set(profile) }
    }

    @Test
    fun `saveUserProfile ничего не делает если пользователь не авторизован`() = runTest(dispatcher) {
        every { mockAuth.currentUser } returns null

        repository.saveUserProfile(UserProfile(fullName = "Test", classRoom = "20ит17"))

        verify(exactly = 0) { mockFirestore.collection(any()) }
    }

    @Test
    fun `getUserProfile испускает UserProfile при получении снапшота`() = runTest(dispatcher) {

        val userId = "uid_456"
        val expectedProfile = UserProfile(fullName = "Мария", classRoom = "13пе14")
        val mockListenerReg = mockk<ListenerRegistration>(relaxed = true)

        every { mockFirestore.collection("users") } returns mockCollectionRef
        every { mockCollectionRef.document(userId) } returns mockDocumentRef

        every {
            mockDocumentRef.addSnapshotListener(any<EventListener<DocumentSnapshot>>())
        } answers {
            val listener = firstArg<EventListener<DocumentSnapshot>>()
            every { mockSnapshot.toObject(UserProfile::class.java) } returns expectedProfile
            listener.onEvent(mockSnapshot, null)
            mockListenerReg
        }

        val result = repository.getUserProfile(userId).first()

        assertEquals(expectedProfile, result)
    }
}