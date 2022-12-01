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
import com.example.yonunca_juegoparabeber.databinding.FragmentCreateGameBinding
import com.example.yonunca_juegoparabeber.online.model.Room
import com.example.yonunca_juegoparabeber.online.viewmodel.CreateGameViewModel

class CreateGameFragment : BaseFragment() {
    private var _binding: FragmentCreateGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateGameViewModel by viewModels()

    override fun setListeners() {
        binding.playButton.setOnClickListener {
            if (isFormValid()){
                viewModel.createRoom(binding.idGame.text.toString())

            }
        }
        binding.backButton.setOnClickListener {
            goBack()
        }
    }

    override fun setObservers() {
        viewModel.getUIState().observe(viewLifecycleOwner){
            it.createdRoom?.let { game ->
                navigateToGame(game)
            }
            shouldShowLoading(it.isLoading)
            it.errorMessage?.let { error ->
                showCreateError(error)
            }
        }
    }

    private fun shouldShowLoading(isLoading: Boolean){
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.playButton.isEnabled = !isLoading
    }

    private fun showCreateError(error: String){
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isFormValid(): Boolean {
        return binding.idGame.text.isNotEmpty()
    }

    private fun navigateToGame(room: Room){
        Toast.makeText(requireContext(), getString(R.string.room_created), Toast.LENGTH_SHORT).show()
        val action = CreateGameFragmentDirections.createGameFragmentToOnlineGameFragment(room)
        findNavController().navigate(action)
    }

}