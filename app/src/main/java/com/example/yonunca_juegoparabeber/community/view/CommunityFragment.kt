package com.example.yonunca_juegoparabeber.community.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yonunca_juegoparabeber.R
import com.example.yonunca_juegoparabeber.base.model.Phrase
import com.example.yonunca_juegoparabeber.base.view.BaseFragment
import com.example.yonunca_juegoparabeber.base.view.PhraseCreatedListener
import com.example.yonunca_juegoparabeber.base.view.PhrasesAdapter
import com.example.yonunca_juegoparabeber.online.view.SearchGameAdapter
import com.example.yonunca_juegoparabeber.community.viewmodel.CommunityViewModel
import com.example.yonunca_juegoparabeber.databinding.FragmentCommunityBinding

class CommunityFragment : BaseFragment(), PhraseCreatedListener {
    private var _binding: FragmentCommunityBinding? = null

    private val binding get() = _binding!!
    private val viewModel: CommunityViewModel by viewModels()

    override fun setListeners() {
        binding.backCommunity.setOnClickListener {
            onBack()
        }
        binding.heart.setOnClickListener {
            viewModel.shouldOrderByPopularity()
        }
        binding.addCommunity.setOnClickListener {
            val action = CommunityFragmentDirections.communityFragmentToCreatePhraseDialogFragment(null)
            findNavController().navigate(action)
        }
    }

    override fun setObservers() {
        viewModel.getUiState().observe(viewLifecycleOwner) {
            if (it != null) {
                updateList(it.phrases)
            }
            setColorOfHeart(it.isSortedByPopularity)
        }
        creationResult()?.observe(viewLifecycleOwner){ success ->
            if (success){
                viewModel.getPhrases()
            }
        }
    }

    private fun updateList(list: List<Phrase>){
        with(binding.recycler){
            if (layoutManager == null){
                setLayoutManager()
            }
            if (adapter == null){
                adapter = PhrasesAdapter(viewModel, list.toMutableList())
            } else {
                (adapter as PhrasesAdapter).updateData(list)
            }
        }
    }

    private fun setLayoutManager(){
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPhrases()
    }

    private fun creationResult() =
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("creationResult")

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onBack() {
        findNavController().navigateUp()
    }

    private fun setColorOfHeart(isSorted: Boolean) {
        if (isSorted) {
            binding.heart.setImageResource(R.drawable.ic_red_heart)
        } else {
            binding.heart.setImageResource(R.drawable.ic_white_heart)
        }
    }

    override fun onPhraseCreated() {
        viewModel.getPhrases()
    }

}