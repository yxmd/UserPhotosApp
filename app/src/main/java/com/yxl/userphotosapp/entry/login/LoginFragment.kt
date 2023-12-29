package com.yxl.userphotosapp.entry.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.yxl.userphotosapp.databinding.FragmentLoginBinding
import com.yxl.userphotosapp.entry.data.EntryRepositoryImpl
import com.yxl.userphotosapp.main.PhotoActivity
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>(factoryProducer = {
        object:ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(EntryRepositoryImpl()) as T
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateLoginPassword()
        checkValid()
        binding.bLogin.setOnClickListener {
            viewModel.login()
            viewModel.token.observe(viewLifecycleOwner){
                if(it.isNotEmpty()){
                    val intent = Intent(requireContext(), PhotoActivity::class.java)
                    intent.putExtra("token", it)
                    intent.putExtra("username", viewModel.login.value)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }

    private fun updateLoginPassword(){
        binding.etPassword.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updatePassword(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.etLogin.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updateLogin(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun checkValid(){
        lifecycleScope.launch {
            viewModel.isFormValid.collect{
                binding.bLogin.isEnabled = it
            }
        }
    }
}