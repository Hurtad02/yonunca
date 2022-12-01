package com.example.yonunca_juegoparabeber.createphrase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.example.yonunca_juegoparabeber.base.view.IBaseUI
import com.example.yonunca_juegoparabeber.createphrase.viewmodel.CreatePhraseDialogViewModel
import com.example.yonunca_juegoparabeber.databinding.FragmentCreatePhraseDialogBinding
import com.example.yonunca_juegoparabeber.utils.HOT_COLLECTION
import com.example.yonunca_juegoparabeber.utils.NAUGHTY_COLLECTION

class CreatePhraseDialogFragment : DialogFragment(), IBaseUI {
    private var _binding: FragmentCreatePhraseDialogBinding? = null
    private val viewModel: CreatePhraseDialogViewModel by viewModels()
    private val args: CreatePhraseDialogFragmentArgs by navArgs()

    private val binding get() = _binding!!
    private val mode = arrayOf("Picante", "Atrevido")

    override fun setListeners() {
        binding.addButton.setOnClickListener {
            if (isFormValid()) {
                if (hasRoom()) {
                    viewModel.updateRoomPhrase(binding.addWord.text.toString())
                } else {
                    viewModel.createPhrase(binding.addWord.text.toString(), getCollectionFromPosition())
                }
            } else {
                setFormError()
            }
        }
    }

    override fun setObservers() {
        viewModel.getUiState().observe(viewLifecycleOwner){
            updateProgressBar(it.isLoading)
            dismissIfRequired(it.shouldDismiss)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePhraseDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        setSpinner()
        if (hasRoom()){
            viewModel.setRoom(args.room!!)
        }
    }

    private fun hasRoom() = args.room != null

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setSpinner() {
        if (!hasRoom()){
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mode)
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinner.adapter = adapter
                }
        } else {
            binding.spinner.visibility = View.GONE
        }
    }

    private fun getCollectionFromPosition(): String {
        return when(binding.spinner.selectedItemPosition){
            0 -> HOT_COLLECTION
            1 -> NAUGHTY_COLLECTION
            else -> HOT_COLLECTION
        }
    }

    private fun dismissIfRequired(shouldDismiss: Boolean) {
        if (shouldDismiss){
            creationResult()?.set("creationResult", true)
            findNavController().navigateUp()
        }
    }

    private fun setFormError() {
        binding.addWord.error = "Por favor ingrese una frase más larga"
    }

    private fun isFormValid(): Boolean {
        return binding.addWord.text.length > 5
    }

    private fun updateProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.addButton.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun creationResult() =
        findNavController().previousBackStackEntry?.savedStateHandle

}