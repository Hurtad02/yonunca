package com.example.yonunca_juegoparabeber.hot.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.yonunca_juegoparabeber.base.model.Phrase
import com.example.yonunca_juegoparabeber.databinding.FragmentOfflineHotScreenBinding
import com.example.yonunca_juegoparabeber.base.view.BaseFragment
import com.example.yonunca_juegoparabeber.hot.viewmodel.OfflineHotScreenViewModel
import com.example.yonunca_juegoparabeber.utils.firebase.speakOut
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.util.*

class OfflineHotScreenFragment : BaseFragment(), TextToSpeech.OnInitListener {
    private var _binding: FragmentOfflineHotScreenBinding? = null

    private val binding get() = _binding!!
    private val viewModel: OfflineHotScreenViewModel by viewModels()
    private var tts: TextToSpeech? = null
    private lateinit var adView: AdView

    override fun setListeners() {
        binding.run {
            nextWord.setOnClickListener {
                viewModel.getNextWord()
            }
            backButton.setOnClickListener {
                onBack()
            }
            previousWord.setOnClickListener {
                viewModel.getPreviousWord()
            }
            icMicrophone.setOnClickListener {
                tts?.speakOut(textWords.text.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(context, this)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOfflineHotScreenBinding.inflate(inflater, container, false)

        MobileAds.initialize(requireContext()) {}

        adView = binding.adView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPhrases()
        disableSpeech()
    }

    override fun setObservers() {
        viewModel.getUiState().observe(viewLifecycleOwner) {
            if (it != null) {
                setPhraseText(it.phrase)
                shouldEnabledSpeech(it.shouldEnableSpeech)
            }
        }
    }

    private fun setPhraseText(phrase: Phrase) {
        binding.textWords.text = phrase.mensaje
    }

    private fun shouldEnabledSpeech(shouldEnable: Boolean) {
        if (!binding.icMicrophone.isEnabled && shouldEnable) {
            enableSpeech()
        }
    }

    private fun disableSpeech() {
        binding.icMicrophone.isEnabled = false
    }

    private fun enableSpeech() {
        binding.icMicrophone.isEnabled = true
    }

    override fun onDestroy() {
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

    private fun onBack() {
        findNavController().navigateUp()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val locale = Locale("es", "ES")
            val result = tts!!.setLanguage(locale)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                binding.icMicrophone.visibility = View.INVISIBLE
            }
        }
    }

}