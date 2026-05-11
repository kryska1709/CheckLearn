package com.example.checklearn

import com.example.checklearn.data.TestRepository
import com.example.checklearn.model.Question
import com.example.checklearn.model.TestResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TestRepositoryTest {

    @MockK lateinit var mockFirestore: FirebaseFirestore
    @MockK lateinit var mockAuth: FirebaseAuth
    @MockK lateinit var mockUser: FirebaseUser
    @MockK lateinit var mockUsersCollection: CollectionReference
    @MockK lateinit var mockUserDoc: DocumentReference
    @MockK lateinit var mockTestsCollection: CollectionReference
    @MockK lateinit var mockTestDoc: DocumentReference
    @MockK lateinit var mockVoidTask: Task<Void>
    @MockK lateinit var mockQuerySnapshot: QuerySnapshot

    private lateinit var repository: TestRepository
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(FirebaseFirestore::class, FirebaseAuth::class)
        every { FirebaseFirestore.getInstance() } returns mockFirestore
        every { FirebaseAuth.getInstance() } returns mockAuth

        // Цепочка: firestore → users → userId → tests
        every { mockFirestore.collection("users") } returns mockUsersCollection
        every { mockUsersCollection.document(any()) } returns mockUserDoc
        every { mockUserDoc.collection("tests") } returns mockTestsCollection

        repository = TestRepository()
    }

    @After
    fun tearDown() = unmockkAll()

    // saveTest: создаёт документ с корректным id
    @Test
    fun `saveTest сохраняет результат с присвоенным id`() = runTest(dispatcher) {
        val testResult = TestResult(
            id = "",
            date = "28-апреля-2026",
            correctAnswers = 8,
            totalQuestions = 10,
            grade = 4,
            questions = listOf()
        )
        val docId = "doc_abc"

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "uid_1"
        every { mockTestsCollection.document() } returns mockTestDoc
        every { mockTestDoc.id } returns docId
        every { mockTestDoc.set(any()) } returns mockVoidTask
        coEvery { mockVoidTask.await() } returns mockk()

        repository.saveTest(testResult)

        coVerify { mockTestDoc.set(testResult.copy(id = docId)) }
    }

    // saveTest: не сохраняет если пользователь не авторизован
    @Test
    fun `saveTest ничего не делает если пользователь не авторизован`() = runTest(dispatcher) {
        every { mockAuth.currentUser } returns null

        repository.saveTest(
            TestResult(
                id = "",
                date = "28-апреля-2026",
                correctAnswers = 8,
                totalQuestions = 10,
                grade = 4,
                questions = listOf()
            )
        )

        verify(exactly = 0) { mockFirestore.collection(any()) }
    }

    // getTestHistory: возвращает список результатов
    @Test
    fun `getTestHistory испускает список TestResult из снапшота`() = runTest(dispatcher) {
        val userId = "uid_2"
        val mockListenerReg = mockk<ListenerRegistration>(relaxed = true)
        val mockDoc = mockk<DocumentSnapshot>()
        val expectedResult = TestResult(
            id = "",
            date = "28-апреля-2026",
            correctAnswers = 8,
            totalQuestions = 10,
            grade = 4,
            questions = listOf()
        )

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns userId
        every { mockDoc.id } returns "doc_1"
        every { mockDoc.toObject(TestResult::class.java) } returns expectedResult.copy(id = "")

        every {
            mockTestsCollection.addSnapshotListener(any<EventListener<QuerySnapshot>>())
        } answers {
            val listener = firstArg<EventListener<QuerySnapshot>>()
            every { mockQuerySnapshot.documents } returns listOf(mockDoc)
            listener.onEvent(mockQuerySnapshot, null)
            mockListenerReg
        }

        val results = repository.getTestHistory().first()

        assertEquals(1, results.size)
        assertEquals("doc_1", results.first().id)
    }

    // deleteTest: удаляет конкретный документ
    @Test
    fun `deleteTest удаляет тест по id`() = runTest(dispatcher) {
        val testId = "test_999"

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "uid_3"
        every { mockTestsCollection.document(testId) } returns mockTestDoc
        every { mockTestDoc.delete() } returns mockVoidTask
        coEvery { mockVoidTask.await() } returns mockk()

        repository.deleteTest(testId)

        coVerify(exactly = 1) { mockTestDoc.delete() }
    }

    // deleteAllTests: удаляет все документы коллекции
    @Test
    fun `deleteAllTests вызывает delete для каждого документа`() = runTest(dispatcher) {
        val userId = "uid_4"
        val mockDoc1 = mockk<QueryDocumentSnapshot>()
        val mockDoc2 = mockk<QueryDocumentSnapshot>()
        val mockDocRef1 = mockk<DocumentReference>()
        val mockDocRef2 = mockk<DocumentReference>()
        val mockGetTask = mockk<Task<QuerySnapshot>>()

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns userId
        every { mockTestsCollection.get() } returns mockGetTask
        coEvery { mockGetTask.await() } returns mockQuerySnapshot
        every { mockQuerySnapshot.iterator() } returns mutableListOf(mockDoc1, mockDoc2).iterator()

        every { mockDoc1.id } returns "id_1"
        every { mockDoc2.id } returns "id_2"
        every { mockTestsCollection.document("id_1") } returns mockDocRef1
        every { mockTestsCollection.document("id_2") } returns mockDocRef2
        every { mockDocRef1.delete() } returns mockVoidTask
        every { mockDocRef2.delete() } returns mockVoidTask
        coEvery { mockVoidTask.await() } returns mockk()

        repository.deleteAllTests()

        coVerify(exactly = 1) { mockDocRef1.delete() }
        coVerify(exactly = 1) { mockDocRef2.delete() }
    }
}