package com.ironledger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironledger.app.data.local.entity.SettingEntity
import com.ironledger.app.data.repository.IronLedgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject

data class AppLockUiState(
    val isPinSet: Boolean = false,
    val isUnlocked: Boolean = false,
    val error: String? = null,
    val failedAttempts: Int = 0,
    val lockoutUntil: Long = 0L,
    val isLoading: Boolean = true
)

@HiltViewModel
class AppLockViewModel @Inject constructor(
    private val repository: IronLedgerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppLockUiState())
    val uiState: StateFlow<AppLockUiState> = _uiState.asStateFlow()

    private val PIN_HASH_KEY = "pin_hash"
    private val FAILED_ATTEMPTS_KEY = "failed_attempts"
    private val LOCKOUT_UNTIL_KEY = "lockout_until"

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            val pinHash = repository.getSetting(PIN_HASH_KEY)
            val failedAttempts = repository.getSetting(FAILED_ATTEMPTS_KEY)?.toIntOrNull() ?: 0
            val lockoutUntil = repository.getSetting(LOCKOUT_UNTIL_KEY)?.toLongOrNull() ?: 0L

            _uiState.value = _uiState.value.copy(
                isPinSet = pinHash != null,
                failedAttempts = failedAttempts,
                lockoutUntil = lockoutUntil,
                isLoading = false
            )
        }
    }

    fun setupPin(pin: String) {
        viewModelScope.launch {
            val hash = hashPin(pin)
            repository.saveSetting(SettingEntity(PIN_HASH_KEY, hash))
            _uiState.value = _uiState.value.copy(isPinSet = true, isUnlocked = true)
        }
    }

    fun onBiometricSuccess() {
        viewModelScope.launch {
            resetFailedAttempts()
            _uiState.value = _uiState.value.copy(isUnlocked = true, error = null)
        }
    }

    fun validatePin(pin: String) {
        if (isLockedOut()) {
            _uiState.value = _uiState.value.copy(error = "Locked out. Try again later.")
            return
        }

        viewModelScope.launch {
            val storedHash = repository.getSetting(PIN_HASH_KEY)
            val inputHash = hashPin(pin)

            if (storedHash == inputHash) {
                resetFailedAttempts()
                _uiState.value = _uiState.value.copy(isUnlocked = true, error = null)
            } else {
                handleFailedAttempt()
            }
        }
    }

    private suspend fun handleFailedAttempt() {
        val newAttempts = _uiState.value.failedAttempts + 1
        var lockoutUntil = 0L
        
        if (newAttempts >= 5) {
            lockoutUntil = System.currentTimeMillis() + (30 * 1000) // 30 seconds lockout
            repository.saveSetting(SettingEntity(LOCKOUT_UNTIL_KEY, lockoutUntil.toString()))
        }
        
        repository.saveSetting(SettingEntity(FAILED_ATTEMPTS_KEY, newAttempts.toString()))
        
        _uiState.value = _uiState.value.copy(
            failedAttempts = newAttempts,
            lockoutUntil = lockoutUntil,
            error = if (newAttempts >= 5) "Too many attempts. Locked out for 30s." else "Incorrect PIN."
        )
    }

    private suspend fun resetFailedAttempts() {
        repository.saveSetting(SettingEntity(FAILED_ATTEMPTS_KEY, "0"))
        repository.saveSetting(SettingEntity(LOCKOUT_UNTIL_KEY, "0"))
        _uiState.value = _uiState.value.copy(failedAttempts = 0, lockoutUntil = 0L)
    }

    private fun isLockedOut(): Boolean {
        return System.currentTimeMillis() < _uiState.value.lockoutUntil
    }

    private fun hashPin(pin: String): String {
        val bytes = pin.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
