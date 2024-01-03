package com.yxl.userphotosapp.entry.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yxl.userphotosapp.databinding.FragmentRegisterBinding
import com.yxl.userphotosapp.main.PhotoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateLoginPassword()
        checkValid()
        var token = ""
        viewModel.token.observe(viewLifecycleOwner){
            token = it
        }
        binding.bSignUp.setOnClickListener {
            viewModel.register()
            viewModel.token.observe(viewLifecycleOwner){
                if(it.isNotEmpty()){
                    val intent = Intent(requireContext(), PhotoActivity::class.java)
                    intent.putExtra("token", token)
                    intent.putExtra("username", viewModel.login.value)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }

        }
    }

    private fun updateLoginPassword(){
        binding.etPassword.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updatePassword(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.etRepPassword.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updateRepPassword(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.etLogin.addTextChangedListener(object: TextWatcher {
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
                binding.bSignUp.isEnabled = it
            }
        }
    }
}