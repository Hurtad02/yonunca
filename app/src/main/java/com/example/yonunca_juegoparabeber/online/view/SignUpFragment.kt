package com.example.yonunca_juegoparabeber.online.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yonunca_juegoparabeber.base.view.BaseFragment
import com.example.yonunca_juegoparabeber.createphrase.view.CreatePhraseDialogFragmentArgs
import com.example.yonunca_juegoparabeber.createphrase.viewmodel.CreatePhraseDialogViewModel
import com.example.yonunca_juegoparabeber.databinding.FragmentCreatePhraseDialogBinding
import com.example.yonunca_juegoparabeber.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment() {

    private var _binding: FragmentSignUpBinding? = null

    private val binding get() = _binding!!

    override fun setListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun setObservers() {

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

}