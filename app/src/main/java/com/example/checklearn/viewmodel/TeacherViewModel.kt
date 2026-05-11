package com.example.checklearn.viewmodel

import androidx.lifecycle.ViewModel
import com.example.checklearn.model.QuestionResult
import com.example.checklearn.model.StudentResult
import com.example.checklearn.model.StudentResultState
import com.example.checklearn.model.TestResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TeacherViewModel() : ViewModel() {
    private val database = FirebaseFirestore.getInstance()
    private val _resultState = MutableStateFlow<StudentResultState>(StudentResultState.Idle)
    val resultState = _resultState.asStateFlow()

    fun searchByClassroom(
        classroom: String
    ) {
        if (classroom.isBlank()) {
            _resultState.value = StudentResultState.Error("Введите номер класса")
            return
        }
        fetchByClassrooms(listOf(classroom.trim()))
    }
    fun loadStudentByTeacher(
        teacherClassroom: List<String>?
    ){
        if (teacherClassroom.isNullOrEmpty()) return
        fetchByClassrooms(teacherClassroom)
    }
    fun parseTestResult(
        data: Map<String, Any>
    ): TestResult{
        @Suppress("UNCHECKED_CAST") val questionList = (data["questions"] as? List<Map<String, Any>>)?.map {
            QuestionResult(
                question = it["question"] as? String ?: "",
                selectedAnswer = (it["selectedAnswer"] as? Long)?.toInt() ?: 0,
                correctAnswer = (it["correctAnswer"] as? Long)?.toInt() ?: 0
            )
        } ?: emptyList()
        return TestResult(
            date = data["date"] as? String ?: "",
            totalQuestions = (data["totalQuestions"] as? Long)?.toInt() ?: 0,
            correctAnswers = (data["correctAnswers"] as? Long)?.toInt() ?: 0,
            grade = (data["grade"] as? Long)?.toInt() ?: 0,
            questions = questionList
        )
    }

    private fun fetchByClassrooms(classrooms: List<String>) {
        _resultState.value = StudentResultState.Loading
        val allResult = mutableListOf<StudentResult>()
        var pendingClasses = classrooms.size

        classrooms.forEach { classroom ->
            database.collection("users")
                .whereEqualTo("classRoom", classroom.trim())
                .get()
                .addOnSuccessListener { userDocs ->
                    var pendingUsers = userDocs.size()

                    if (pendingUsers == 0) {
                        pendingClasses--
                        if (pendingClasses == 0) {
                            _resultState.value = StudentResultState.Success(allResult)
                        }
                        return@addOnSuccessListener
                    }

                    userDocs.forEach { userDoc ->
                        val studentName = userDoc.getString("fullName") ?: "Имя не известно"
                        val studentClass = userDoc.getString("classRoom") ?: "Класс не известен"

                        database.collection("users")
                            .document(userDoc.id)
                            .collection("tests")
                            .get()
                            .addOnSuccessListener { testDocs ->
                                testDocs.forEach { testDoc ->
                                    allResult.add(
                                        StudentResult(
                                            studentName = studentName,
                                            classRoom = studentClass,
                                            testId = testDoc.id,
                                            testResult = parseTestResult(testDoc.data)
                                        )
                                    )
                                }
                                pendingUsers--
                                if (pendingUsers == 0) {
                                    pendingClasses--
                                    if (pendingClasses == 0) {
                                        _resultState.value = StudentResultState.Success(allResult)
                                    }
                                }
                            }
                            .addOnFailureListener {
                                pendingUsers--
                                if (pendingUsers == 0) {
                                    pendingClasses--
                                    if (pendingClasses == 0) {
                                        _resultState.value = StudentResultState.Success(allResult)
                                    }
                                }
                            }
                    }
                }
                .addOnFailureListener { e ->
                    pendingClasses--
                    if (pendingClasses == 0) {
                        _resultState.value = StudentResultState.Error(e.message.toString())
                    }
                }
        }
    }


}