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
    ){
        if (classroom.isBlank()){
            _resultState.value = StudentResultState.Error("Введите номер класса")
            return
        }
        _resultState.value = StudentResultState.Loading
        database.collection("users")
            .whereEqualTo("classRoom",classroom.trim())
//            .whereEqualTo("teacher", false)
            .get()
            .addOnSuccessListener { userDocs ->
                if (userDocs.isEmpty){
                    _resultState.value = StudentResultState.Error("Ученики класса $classroom не найдены")
                    return@addOnSuccessListener
                }
                val allResult = mutableListOf<StudentResult>()
                var count = userDocs.size()
                userDocs.forEach{
                    val studentName = it.getString("fullName") ?: "Имя не известно"
                    val classroom = it.getString("classRoom") ?: "Класс не известен"
                    database.collection("users")
                        .document(it.id)
                        .collection("tests")
                        .get()
                        .addOnSuccessListener {  testDocs ->
                            testDocs.forEach{
                                allResult.add(
                                    StudentResult(
                                        studentName = studentName,
                                        classRoom = classroom,
                                        testId = it.id,
                                        testResult = parseTestResult(it.data)
                                    )
                                )
                            }
                            count--
                            if (count == 0){
                                _resultState.value = StudentResultState.Success(
                                    allResult
                                )
                            }
                        }
                        .addOnFailureListener {
                            count--
                            if (count == 0){
                                _resultState.value = StudentResultState.Success(
                                    allResult
                                )
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
               _resultState.value = StudentResultState.Error(e.message.toString())
            }
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
}