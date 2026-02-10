package com.example.checklearn.model

sealed class HistoryUIState {
    data object Unauthorized: HistoryUIState()
    data object NeedProfileInfo: HistoryUIState()
    data object ShowHistory: HistoryUIState()
}