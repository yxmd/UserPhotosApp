package com.yxl.userphotosapp.entry.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.entry.data.EntryRepositoryImpl
import com.yxl.userphotosapp.entry.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: EntryRepositoryImpl
) : ViewModel() {

    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login

    private val _password = MutableStateFlow("")

    private val _repPassword = MutableStateFlow("")

    val isFormValid: StateFlow<Boolean> =
        combine(_password, _repPassword, _login) { password, rep, login ->
            password.length >= 8 && rep.length >= 8 && password == rep && login.isNotEmpty()
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    val token = MutableLiveData("")

    fun register() {
        viewModelScope.launch(Dispatchers.IO)  {
            repository.register(User(_login.value, _repPassword.value)).collect {
                when (it) {
                    is Result.Error -> {

                    }

                    is Result.Success -> {
                        it.data?.let { user ->
                            token.postValue(user.data.token)
                        }

                    }
                }
            }
        }

    }

    fun updateLogin(s: String) {
        _login.value = s
    }

    fun updatePassword(s: String) {
        _password.value = s
    }

    fun updateRepPassword(s: String) {
        _repPassword.value = s
    }

}