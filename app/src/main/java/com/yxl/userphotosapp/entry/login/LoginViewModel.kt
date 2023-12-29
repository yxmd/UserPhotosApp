package com.yxl.userphotosapp.entry.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.entry.data.EntryRepository
import com.yxl.userphotosapp.entry.model.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: EntryRepository
): ViewModel() {

    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login

    private val _password = MutableStateFlow("")

    val isFormValid: StateFlow<Boolean> = combine(_password, _login){ password, login ->
        password.length >= 8 && login.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    val token = MutableLiveData("")

    fun updatePassword(s: String){
        _password.value = s
    }
    fun updateLogin(newVal: String){
        _login.value = newVal
    }

    fun login(){
        viewModelScope.launch {
            repository.login(User(_login.value, _password.value)).collect{
                when(it){
                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                        Log.d("errorViewModel", it.data.toString())
                    }
                    is Result.Success -> {
                        it.data?.let {user ->
                            token.postValue(user.data.token)
                            Log.d("loginViewmodel", user.toString())

                        }

                    }
                }
            }
        }
        Log.d("loginViewmodel", token.value.toString())

    }
}