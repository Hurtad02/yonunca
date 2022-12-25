package com.example.yonunca_juegoparabeber.online.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yonunca_juegoparabeber.R
import com.example.yonunca_juegoparabeber.base.view.BaseFragment
import com.example.yonunca_juegoparabeber.createphrase.view.CreatePhraseDialogFragmentArgs
import com.example.yonunca_juegoparabeber.createphrase.viewmodel.CreatePhraseDialogViewModel
import com.example.yonunca_juegoparabeber.databinding.FragmentCreatePhraseDialogBinding
import com.example.yonunca_juegoparabeber.databinding.FragmentSignUpBinding
import com.example.yonunca_juegoparabeber.online.viewmodel.SignUpViewModel
import com.example.yonunca_juegoparabeber.utils.isValidEmail
import com.example.yonunca_juegoparabeber.utils.isValidPassword

class SignUpFragment : BaseFragment() {

    private var _binding: FragmentSignUpBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun setListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.signUpButton.setOnClickListener {
            if (isFormValid()){
                viewModel.signUp(binding.email.text.toString(), binding.password.text.toString())
            }
        }
    }

    private fun isFormValid(): Boolean {
        var result = true
        with(binding){
            if (!email.text.isValidEmail()){
                showEmailError()
                result = false
            }
            if (!password.text.isValidPassword()){
                showPasswordError()
                result = false
            }
        }
        return result
    }

    private fun showEmailError(){
        binding.email.error = getString(R.string.invalid_email)
    }

    private fun showPasswordError(){
        binding.password.error = getString(R.string.invalid_password)
    }

    override fun setObservers() {
        viewModel.getUIState().observe(viewLifecycleOwner){
            if (it.result){
                goHome()
            }
            shouldShowLoading(it.isLoading)
            it.signUpError?.let { error ->
                showToast(error)
                viewModel.clearError()
            }
        }
    }

    private fun shouldShowLoading(isLoading: Boolean){
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun goHome(){
        findNavController().navigate(R.id.action_global_mainFragment)
    }

}