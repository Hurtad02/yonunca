package com.example.yonunca_juegoparabeber.online.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    override fun setObservers() {
        viewModel.getUIState().observe(viewLifecycleOwner){
            if (it.roomsList.isNotEmpty()){
                updateList(it.roomsList)
            }
            updateProgress(it.isLoading)
        }
    }

    private fun updateProgress(isLoading: Boolean){
        binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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