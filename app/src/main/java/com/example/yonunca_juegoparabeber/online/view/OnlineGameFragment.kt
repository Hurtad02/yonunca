package com.example.yonunca_juegoparabeber.online.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.example.yonunca_juegoparabeber.R
import com.example.yonunca_juegoparabeber.base.BaseApplication
import com.example.yonunca_juegoparabeber.base.view.BaseFragment
import com.example.yonunca_juegoparabeber.databinding.FragmentOnlineGameBinding
import com.example.yonunca_juegoparabeber.online.viewmodel.OnlineGameViewModel

class OnlineGameFragment : BaseFragment() {
    private var _binding: FragmentOnlineGameBinding? = null

    private val viewModel: OnlineGameViewModel by viewModels()
    private val args: OnlineGameFragmentArgs by navArgs()
    private val binding get() = _binding!!


    override fun setListeners() {
        binding.write.setOnClickListener {
            val action = OnlineGameFragmentDirections
                .actionOnlineGameFragmentToCreatePhraseDialogFragment(room = viewModel.getCurrentRoom())
            findNavController().navigate(action)
        }
        binding.backOnlineGame.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.random.setOnClickListener {
            viewModel.getRandomPhrase()
        }
    }

    override fun setObservers() {
        viewModel.getUIState().observe(viewLifecycleOwner){
            it.room?.let { room ->
                viewModel.joinCurrentPlayerToGameIfRequired()
                updatePhrase(room.phrase)
                setTurn(viewModel.isYourTurn())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnlineGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGameData(args.room)
    }

    override fun onDestroy() {
        viewModel.removePlayer()
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updatePhrase(text: String) {
        binding.phrase.text = text
    }

    private fun setTurn(isYourTurn: Boolean){
        if (isYourTurn){
            binding.turnText.text = getText(R.string.turn)
            enableWriteButton()
            enableRandomButton()
            pauseAnimation()
        } else {
            binding.turnText.text = getText(R.string.other_turn)
            disableWriteButton()
            disableRandomButton()
            startAnimation()
        }
    }

    private fun startAnimation() {
        val animationView = binding.animationView
        animationView.setAnimation("lottie_waiting.json")
        animationView.loop(true)
        animationView.playAnimation()
    }

    private fun pauseAnimation() {
        val animationView = binding.animationView
        animationView.setAnimation("lottie_write.json")
        animationView.loop(true)
        animationView.playAnimation()
    }

    private fun enableWriteButton() {
        val color = ContextCompat.getColor(requireContext(), R.color.button_turn)
        binding.write.isEnabled = true
        binding.write.setBackgroundColor(color)
    }

    private fun enableRandomButton() {
        val color = ContextCompat.getColor(requireContext(), R.color.button_turn)
        binding.random.isEnabled = true
        binding.random.setBackgroundColor(color)
    }

    private fun disableWriteButton() {
        val grayColor = ContextCompat.getColor(requireContext(), R.color.gray_light_trans)
        binding.write.isEnabled = false
        binding.write.setBackgroundColor(grayColor)
    }

    private fun disableRandomButton() {
        val grayColor = ContextCompat.getColor(requireContext(), R.color.gray_light_trans)
        binding.random.isEnabled = false
        binding.random.setBackgroundColor(grayColor)
    }
}