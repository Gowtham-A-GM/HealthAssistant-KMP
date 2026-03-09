package com.example.healthassistant.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.local.report.ReportLocalDataSource
import com.example.healthassistant.domain.model.assessment.Report
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val reportLocal: ReportLocalDataSource
) : ViewModel() {

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports

    init {
        loadReports()
    }

    fun loadReports() {
        viewModelScope.launch {
            _reports.value = reportLocal.getAll()
        }
    }
}
