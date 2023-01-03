package com.example.yonunca_juegoparabeber.online.view

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yonunca_juegoparabeber.R
import com.example.yonunca_juegoparabeber.base.model.Phrase
import com.example.yonunca_juegoparabeber.base.model.RoomModel
import com.example.yonunca_juegoparabeber.base.view.BaseFragment
import com.example.yonunca_juegoparabeber.databinding.FragmentOnlineGameBinding
import com.example.yonunca_juegoparabeber.online.model.Room
import com.example.yonunca_juegoparabeber.online.viewmodel.OnlineGameViewModel
import com.example.yonunca_juegoparabeber.utils.firebase.speakOut
import java.util.*

class OnlineGameFragment : BaseFragment(), TextToSpeech.OnInitListener {
    private var _binding: FragmentOnlineGameBinding? = null

    private val viewModel: OnlineGameViewModel by viewModels()
    private val args: OnlineGameFragmentArgs by navArgs()
    private val binding get() = _binding!!

    private var tts: TextToSpeech? = null

    override fun setListeners() {
        binding.run {
            popUp.write.setOnClickListener {
                val action = OnlineGameFragmentDirections
                    .actionOnlineGameFragmentToCreatePhraseDialogFragment(room = viewModel.getCurrentRoom())
                findNavController().navigate(action)
            }
            backOnlineGame.setOnClickListener {
                findNavController().navigateUp()
            }
            popUp.random.setOnClickListener {
                viewModel.getRandomPhrase()
            }
            microphone.setOnClickListener {
                tts?.speakOut(phrase.text.toString())
            }
        }
    }

    override fun setObservers() {
        viewModel.getUIState().observe(viewLifecycleOwner){
            it.room?.let { room ->
                viewModel.joinCurrentPlayerToGameIfRequired()
                updatePhrase(room.phrase)
                setTurn(viewModel.isYourTurn())
                showCurrentTurn()
                showPlayersNumber()
                listOfPlayers()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(context, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGameData(args.room)
    }

    override fun onDestroy() {
        viewModel.removePlayer()
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCurrentTurn() {
        binding.currentTurn.text = getString(R.string.current_turn, viewModel.getCurrentTurn())
    }

    private fun showPlayersNumber() {
        binding.numberPlayer.text = getString(R.string.number_player, viewModel.getPlayersNumber())
    }

    private fun listOfPlayers() {
        binding.players.text = getString(R.string.list_players, viewModel.playersList())
    }

    private fun updatePhrase(text: String) {
        binding.phrase.text = text
    }

    private fun setTurn(isYourTurn: Boolean){
        if (isYourTurn){
            binding.microphone.visibility = View.GONE
            binding.popUp.createWord.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE
        } else {
            binding.microphone.visibility = View.VISIBLE
            binding.popUp.createWord.visibility = View.GONE
            startAnimation()
        }
    }

    private fun startAnimation() {
        val animationView = binding.animationView
        animationView.visibility = View.VISIBLE
        animationView.setAnimation("lottie_waiting.json")
        animationView.loop(true)
        animationView.playAnimation()
    }

//    private fun enableButtons() {
//        binding.background.visibility = View.VISIBLE
//        binding.write.visibility = View.VISIBLE
//        binding.pencil.visibility = View.VISIBLE
//        binding.random.visibility = View.VISIBLE
//        binding.dices.visibility = View.VISIBLE
//    }
//
//    private fun disableButtons() {
//        binding.background.visibility = View.GONE
//        binding.write.visibility = View.GONE
//        binding.pencil.visibility = View.GONE
//        binding.random.visibility = View.GONE
//        binding.dices.visibility = View.GONE
//    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val locale = Locale("es", "ES")
            val result = tts!!.setLanguage(locale)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                binding.microphone.visibility = View.INVISIBLE
            }
        }
    }
}