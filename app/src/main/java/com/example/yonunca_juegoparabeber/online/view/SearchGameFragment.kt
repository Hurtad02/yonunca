package com.example.yonunca_juegoparabeber.online.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yonunca_juegoparabeber.R
import com.example.yonunca_juegoparabeber.base.view.BaseFragment
import com.example.yonunca_juegoparabeber.databinding.FragmentSearchGameBinding
import com.example.yonunca_juegoparabeber.online.model.Room
import com.example.yonunca_juegoparabeber.online.viewmodel.SearchGameViewModel

class SearchGameFragment : BaseFragment(), OnJoinPress {
    private var _binding: FragmentSearchGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchGameViewModel by viewModels()

    override fun setListeners() {
        binding.backButton.setOnClickListener {
            goBack()
        }
        binding.joinButton.setOnClickListener {
            if (isFormValid()){
                viewModel.getRoomByCode(code = binding.idPrivateGame.text.toString())
            } else {
                showFormErrors()
            }
        }
    }

    private fun showFormErrors(){
        binding.idPrivateGame.error = getString(R.string.invalid_private_code)
    }

    private fun isFormValid(): Boolean {
        return binding.idPrivateGame.text.length == 6
    }

    override fun setObservers() {
        viewModel.getUIState().observe(viewLifecycleOwner){
            // Update room list
            if (it.roomsList.isNotEmpty()){
                updateList(it.roomsList)
            }
            // Update loading bar
            updateProgress(it.isLoading)
            // Show error if exist
            if (it.roomError){
                showPrivateRoomError()
                viewModel.clearError()
            }
            // Join private room if exist
            if (it.privateRoom != null){
                onJoinPressed(room = it.privateRoom)
                viewModel.clearRoom()
                binding.idPrivateGame.text.clear()
            }
        }
    }

    private fun showPrivateRoomError(){
        Toast.makeText(requireContext(), getString(R.string.room_not_found), Toast.LENGTH_SHORT).show()
    }

    private fun updateProgress(isLoading: Boolean){
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRooms()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateList(list: List<Room>) {
        with(binding.recyclerSearch){
            if (layoutManager == null){
                setLayoutManager()
            }
            if (adapter == null){
                adapter = SearchGameAdapter(this@SearchGameFragment, list.toMutableList())
            } else {
                (adapter as SearchGameAdapter).updateData(list)
            }
        }
    }

    private fun setLayoutManager(){
        binding.recyclerSearch.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onJoinPressed(room: Room) {
        val action = SearchGameFragmentDirections.playerSelectionFragmentToOnlineGameFragment(room)
        findNavController().navigate(action)
    }

}