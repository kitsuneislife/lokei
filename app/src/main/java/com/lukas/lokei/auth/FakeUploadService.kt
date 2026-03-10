package com.lukas.lokei.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UploadState(
    val progress: Int = 0,
    val isUploading: Boolean = false,
    val isCompleted: Boolean = false
)

class FakeUploadService(private val scope: CoroutineScope) {
    private val cpfState = MutableStateFlow(UploadState())
    private val cnhState = MutableStateFlow(UploadState())

    fun cpfUploadState(): StateFlow<UploadState> = cpfState.asStateFlow()

    fun cnhUploadState(): StateFlow<UploadState> = cnhState.asStateFlow()

    fun startCpfUpload() = startUpload(cpfState)

    fun startCnhUpload() = startUpload(cnhState)

    fun resetAll() {
        cpfState.value = UploadState()
        cnhState.value = UploadState()
    }

    private fun startUpload(target: MutableStateFlow<UploadState>) {
        if (target.value.isUploading) return
        scope.launch {
            target.value = UploadState(progress = 0, isUploading = true, isCompleted = false)
            for (step in 1..10) {
                delay(140)
                target.value = UploadState(
                    progress = step * 10,
                    isUploading = step < 10,
                    isCompleted = step == 10
                )
            }
        }
    }
}
